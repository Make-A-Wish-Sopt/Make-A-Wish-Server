package com.sopterm.makeawish.service;

import static com.sopterm.makeawish.common.Util.*;
import static com.sopterm.makeawish.common.message.ErrorMessage.*;
import static com.sopterm.makeawish.dto.wish.WishStatus.*;
import static java.util.Objects.*;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

import com.sopterm.makeawish.dto.wish.WishStatus;
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
		val from = convertToTime(requestDTO.startDate());
		val to = convertToTime(requestDTO.endDate());
		val now = LocalDateTime.now();
		if (from.isBefore(now) || to.isBefore(now)) {
			throw new IllegalArgumentException(PAST_WISH.getMessage());
		}
		if (wishRepository.existsConflictWish(wisher, from, to, EXPIRY_DAY)) {
			throw new IllegalArgumentException(EXIST_MAIN_WISH.getMessage());
		}
		val wish = requestDTO.toEntity(wisher);
		return wishRepository.save(wish).getId();
	}

	@Transactional
	public UserCurrentWishResponseDTO updateUserMainWish(Long userId, UserWishUpdateRequestDTO request) {
		val wisher = getUser(userId);
		val userWish = getUserWish(userId);
		val status = checkUpdatableDate(wisher, userWish);
		if(status.equals(END)) {
			throw new IllegalArgumentException(NOT_CURRENT_WISH.getMessage());
		}
		if (status.equals(BEFORE)) {
			userWish.updateWish(
					nonNull(request.startDate()) ? convertToTime(request.startDate()) : null,
					nonNull(request.endDate()) ? convertToTime(request.endDate()) : null,
					request.phone());
		} else if(status.equals(WHILE)) {
			userWish.updateWish(null, null, request.phone());
		}
		wisher.updateMemberProfile(request.name(), request.bankName(), request.account(), request.phone());
		return UserCurrentWishResponseDTO.from(userWish, wisher);
	}

	public WishResponseDTO findWish(Long wishId) throws AccessDeniedException {
		val wish = getWish(wishId);
		if (wish.getEndAt().isBefore(LocalDateTime.now())) {
			throw new AccessDeniedException(EXPIRE_WISH.getMessage());
		}
		return WishResponseDTO.from(wish);
	}

	public UserCurrentWishResponseDTO getUserMainWish(Long userId) {
		val wisher = getUser(userId);
		if (!wishRepository.existsWishByWisher(wisher)) {
			throw new IllegalArgumentException(NO_WISH.getMessage());
		}
		val wish = wishRepository.findMainWish(wisher, 0).orElseThrow(() -> new IllegalArgumentException(EXPIRE_WISH.getMessage()));
		return nonNull(wish) ? UserCurrentWishResponseDTO.from(wish, wisher) : null;
	}

	public Wish getUserWish(Long userId) {
		if(!wishRepository.existsWishByWisher(getUser(userId))) {
			throw new IllegalArgumentException(NO_EXIST_MAIN_WISH.getMessage());
		}
		return wishRepository.findMainWish(getUser(userId), 0).orElse(null);
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

	@Transactional
	public void deleteWishes(Long userId, WishIdRequestDTO requestDTO) {
		val user = getUser(userId);
		val wishIds = filterUserWishes(user, requestDTO.wishes());
		wishRepository.deleteAllById(wishIds);
	}

	public WishesResponseDTO findWishes(Long userId) {
		val wishes = wishRepository.findByWisherOrderByStartAtDesc(getUser(userId));
		return WishesResponseDTO.of(wishes);
	}

	private WishStatus checkUpdatableDate(User wisher, Wish userWish) {
		val now = LocalDateTime.now();
		if (userWish.getEndAt().isBefore(now)) {
			return END;
		}
		else if (userWish.getEndAt().isAfter(now) && userWish.getStartAt().isBefore(now)) {
			return WHILE;
		}
		return BEFORE;
	}

	private User getUser(Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException(INVALID_USER.getMessage()));
	}

	private List<Long> filterUserWishes(User user, List<Long> wishIds) {
		return wishIds.stream()
			.filter(wishId -> getWish(wishId).getWisher().equals(user))
			.toList();
	}
}