package com.sopterm.makeawish.service;

import static com.sopterm.makeawish.common.Util.*;
import static com.sopterm.makeawish.common.message.ErrorMessage.*;
import static java.util.Objects.*;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

import com.sopterm.makeawish.domain.wish.WishStatus;
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
		if (wishRepository.findMainWish(wisher, EXPIRY_DAY).isPresent()) {
			throw new IllegalStateException(EXIST_MAIN_WISH.getMessage());
		}
		val from = convertToDate(requestDTO.startDate());
		val to = convertToDate(requestDTO.endDate());
		validateWishDate(wisher, from , to);
		val wish = requestDTO.toEntity(wisher);
		return wishRepository.save(wish).getId();
	}

	public WishResponseDTO findWish(Long wishId) throws AccessDeniedException {
		val wish = getWish(wishId);
		if (wish.getEndAt().isBefore(LocalDateTime.now())) {
			throw new AccessDeniedException(EXPIRE_WISH.getMessage());
		}
		return WishResponseDTO.from(wish);
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
		val wishes = wishRepository.findEndWishes(getUser(userId), EXPIRY_DAY);
		return WishesResponseDTO.of(wishes);
	}

	private User getUser(Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException(INVALID_USER.getMessage()));
	}

	private void validateWishDate(User wisher, LocalDateTime from, LocalDateTime to) {
		val now = LocalDateTime.now().toLocalDate().atStartOfDay();
		if (from.isBefore(now) || to.isBefore(now)) {
			throw new IllegalArgumentException(PAST_WISH.getMessage());
		}
		if (from.isAfter(to)) {
			throw new IllegalArgumentException(INVALID_TERM.getMessage());
		}
		if (wishRepository.existsConflictWish(wisher, from, to, EXPIRY_DAY)) {
			throw new IllegalArgumentException(EXIST_MAIN_WISH.getMessage());
		}
	}

	private List<Long> filterUserWishes(User user, List<Long> wishIds) {
		return wishIds.stream()
			.filter(wishId -> isDeletable(user, wishId))
			.toList();
	}

	private boolean isDeletable(User user, Long wishId) {
		val wish = getWish(wishId);
		return wish.getWisher().equals(user) && wish.getStatus(EXPIRY_DAY).equals(WishStatus.END);
	}

	@Transactional
	public void stopWish(Long userId, Long wishId) throws AccessDeniedException {
		val wish = getWish(wishId);

		if(!wish.getWisher().getId().equals(userId))
			throw new AccessDeniedException(FORBIDDEN.getMessage());

		if(wish.getStartAt().isAfter(LocalDateTime.now())){
			wishRepository.deleteById(wishId);
		}else{
			wish.updateTerm(wish.getStartAt(), LocalDateTime.now().minusDays(1));
		}
	}
}