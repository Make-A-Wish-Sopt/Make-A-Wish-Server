package com.sopterm.makeawish.service;

import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.domain.wish.Wish;
import com.sopterm.makeawish.domain.wish.WishStatus;
import com.sopterm.makeawish.dto.wish.*;
import com.sopterm.makeawish.repository.UserRepository;
import com.sopterm.makeawish.repository.wish.WishRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

import static com.sopterm.makeawish.common.Util.convertToDate;
import static com.sopterm.makeawish.common.message.ErrorMessage.*;
import static com.sopterm.makeawish.domain.wish.WishStatus.*;
import static java.util.Objects.nonNull;

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
		validateWishDate(wisher, from, to);
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

	@Transactional
	public UserWishUpdateResponseDTO updateUserMainWish(Long userId, UserWishUpdateRequestDTO request) {
		val wisher = getUser(userId);
		val wish = getUserMainWish(wisher);
		val status = wish.getStatus(0);

		if (status.equals(END)) {
			throw new IllegalArgumentException(NOT_CURRENT_WISH.getMessage());
		}
		if (status.equals(BEFORE)) {
			val startDate = nonNull(request.startDate()) ? convertToDate(request.startDate()) : null;
			val endDate = nonNull(request.endDate()) ? convertToDate(request.endDate()) : null;
			wish.updateTerm(startDate, endDate);
			wish.updateContent(request.imageUrl(), request.price(), request.title(), request.hint(), request.initial());
		}
		if (status.equals(BEFORE) || status.equals(WHILE)) {
			wisher.updateProfile(request.name(), request.bankName(), request.account(), request.phone());
		}

		return UserWishUpdateResponseDTO.of(wisher, wish);
	}

	public UserWishUpdateResponseDTO findUserMainWish(Long userId) {
		val wisher = getUser(userId);
		val wish = getUserMainWish(wisher);
		return UserWishUpdateResponseDTO.of(wisher, wish);
	}

	@Transactional
	public void stopWish(Long userId) throws AccessDeniedException {
		val user = getUser(userId);
		val wish = getUserMainWish(user);

		val status = wish.getStatus(0);
		switch(status) {
			case BEFORE -> wishRepository.delete(wish);
			case WHILE -> wish.updateTerm(null, LocalDateTime.now().toLocalDate().atStartOfDay().minusDays(1));
			default -> throw new AccessDeniedException(EXPIRE_WISH.getMessage());
		}
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

	private Wish getUserMainWish(User user) {
		return wishRepository
			.findMainWish(user, 0)
			.orElseThrow(() -> new EntityNotFoundException(NO_WISH.getMessage()));
	}
}