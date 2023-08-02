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
	public MypageWishUpdateResponseDTO updateWish(Long userId, MypageWishUpdateRequestDTO request) {
		val user = getUser(userId);
		if (!wishRepository.existsWishByWisher(user)) {
			throw new IllegalArgumentException(NO_EXIST_MAIN_WISH.getMessage());
		}
		user.updateMemberProfile(convertToTime(request.birthStartAt()), convertToTime(request.birthEndAt()), request.name(), request.bankName(), request.account(), request.phone());

		val userWish = getUserWish(userId);
		if (nonNull(userWish)) {
			userWish.updateWish(convertToTime(request.birthStartAt()), convertToTime(request.birthEndAt()), request.phone());
		}
		return MypageWishUpdateResponseDTO.from(userWish);
	}

	public WishResponseDTO findWish(Long wishId) {
		return WishResponseDTO.from(getWish(wishId));
	}

	public MypageWishUpdateResponseDTO getMypageWish(Long userId) {
		val wish = wishRepository
				.findFirstByWisherOrderByEndAtDesc(getUser(userId)).orElse(null);
		return nonNull(wish) ? MypageWishResponseDTO.from(wish, wisher) : null;
	}

	public Wish getUserWish(Long userId) {
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