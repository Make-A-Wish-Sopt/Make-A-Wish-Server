package com.sopterm.makeawish.service;

import static com.sopterm.makeawish.common.message.ErrorMessage.*;
import static java.util.Objects.*;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.sopterm.makeawish.dto.wish.*;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.domain.wish.Wish;
import com.sopterm.makeawish.repository.UserRepository;
import com.sopterm.makeawish.repository.wish.WishRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishService {

	private final WishRepository wishRepository;
	private final UserRepository userRepository;

	private final int EXPIRY_DAY = 7;

	@Transactional
	public Long createWish(Long userId, WishRequestDTO requestDTO) {
		val wisher = getUser(userId);
		val from = WishRequestDTO.convertToTime(requestDTO.startDate());
		val to = WishRequestDTO.convertToTime(requestDTO.endDate());
		if (wishRepository.existsConflictWish(wisher, from, to, EXPIRY_DAY)) {
			throw new IllegalArgumentException(EXIST_MAIN_WISH.getMessage());
		}
		val wish = requestDTO.toEntity(wisher);
		return wishRepository.save(wish).getId();
	}

	@Transactional
	public UserCurrentWishResponseDTO updateWish(Long userId, UserWishUpdateRequestDTO request) {
		val wisher = getUser(userId);
		val userWish = getUserWish(userId);
		if (nonNull(wishRepository.findWishIsNowAvailable(wisher))) {
			throw new IllegalArgumentException(NOT_CURRENT_WISH.getMessage());
		}
		wisher.updateMemberProfile(convertToTime(request.startDate()), convertToTime(request.endDate()), request.name(), request.bankName(), request.account(), request.phone());
		userWish.updateWish(convertToTime(request.startDate()), convertToTime(request.endDate()), request.phone());
		return UserCurrentWishResponseDTO.from(userWish, wisher);
	}

	public WishResponseDTO findWish(Long wishId) {
		return WishResponseDTO.from(getWish(wishId));
	}

	public UserCurrentWishResponseDTO getCurrentUserWish(Long userId) {
		val wisher = getUser(userId);
		if (!wishRepository.existsWishByWisher(wisher)) {
			throw new IllegalArgumentException(NO_EXIST_MAIN_WISH.getMessage());
		}
		if (!nonNull(wishRepository.findWishIsNowAvailable(wisher))) {
			throw new IllegalArgumentException(EXPIRE_WISH.getMessage());
		}
		val wish = wishRepository
				.findFirstByWisherOrderByEndAtDesc(getUser(userId)).orElse(null);
		return nonNull(wish) ? UserCurrentWishResponseDTO.from(wish, wisher) : null;
	}

	public Wish getUserWish(Long userId) {
		if(!wishRepository.existsWishByWisher(getUser(userId))) {
			throw new IllegalArgumentException(NO_EXIST_MAIN_WISH.getMessage());
		}
		return wishRepository.findFirstByWisherOrderByEndAtDesc(getUser(userId)).orElse(null);
	}

	public MainWishResponseDTO findMainWish(Long userId) {
		val wish = wishRepository
			.findMainWish(getUser(userId), EXPIRY_DAY)
			.orElse(null);
		return nonNull(wish) ? MainWishResponseDTO.from(wish) : null;
	}

	public Wish getWish(Long id) {
		return wishRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(INVALID_WISH.getMessage()));
	}

	public String getPresentInfo(String url, String tag) throws IOException {
		return Jsoup.connect(url)
			.timeout(5000).get()
			.select("." + tag)
			.toString();
	}

	public UserWishResponseDTO findWish(Long userId, Long wishId) throws AccessDeniedException {
		val wish = getWish(wishId);
		if (!wish.getWisher().getId().equals(userId)) {
			throw new AccessDeniedException(FORBIDDEN.getMessage());
		}
		return UserWishResponseDTO.of(wish);
	}

	public WishesResponseDTO findWishes(Long userId) {
		val wishes = wishRepository.findByWisherOrderByStartAtDesc(getUser(userId));
		return WishesResponseDTO.of(wishes);
	}

	private User getUser(Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException(INVALID_USER.getMessage()));
	}

	private LocalDateTime convertToTime(String date) {
		val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
		return LocalDateTime.parse(date + " 00:00", formatter);
	}
}