package com.sopterm.makeawish.service;

import static com.sopterm.makeawish.common.message.ErrorMessage.*;
import static java.util.Objects.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.sopterm.makeawish.dto.wish.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.domain.wish.Wish;
import com.sopterm.makeawish.repository.UserRepository;
import com.sopterm.makeawish.repository.WishRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishService {

	private final WishRepository wishRepository;
	private final UserRepository userRepository;

	@Transactional
	public Long createWish(Long userId, WishRequestDTO requestDTO) {
		User wisher = getUser(userId);
		if (wishRepository.existsMainWish(wisher, LocalDateTime.now())) {
			throw new IllegalArgumentException(EXIST_MAIN_WISH.getMessage());
		}
		Wish wish = requestDTO.toEntity(wisher);
		return wishRepository.save(wish).getId();
	}

	@Transactional
	public MypageWishUpdateResponseDTO updateWish(Long userId, MypageWishUpdateRequestDTO request) {
		User user = getUser(userId);
		if (!wishRepository.existsWishByWisherOrderByEndAtDesc(user)) {
			throw new IllegalArgumentException(NO_EXIST_MAIN_WISH.getMessage());
		}
		user.updateMemberProfile(convertToTime(request.birthStartAt()), convertToTime(request.birthEndAt()), request.name(), request.bankName(), request.account(), request.phone());
		userRepository.save(user);

		Wish userWish = getUserWish(userId);
		if (nonNull(userWish)) {
			userWish.updateWish(convertToTime(request.birthStartAt()), convertToTime(request.birthEndAt()), request.name(), request.bankName(), request.account(), request.phone());
		}
		System.out.println("HDISFJIEWOF");
		System.out.println(userWish.getEndAt());
		return MypageWishUpdateResponseDTO.from(wishRepository.save(userWish));
	}

	public WishResponseDTO findWish(Long wishId) {
		return WishResponseDTO.from(getWish(wishId));
	}

	public MypageWishUpdateResponseDTO getMypageWish(Long userId) {
		Wish wish = wishRepository
				.findFirstByWisherOrderByEndAtDesc(getUser(userId));
		System.out.println(wish.getEndAt());
		return MypageWishUpdateResponseDTO.from(wish);
	}

	public Wish getUserWish(Long userId) {
		return wishRepository.findFirstByWisherOrderByEndAtDesc(getUser(userId));
	}

	public MainWishResponseDTO findMainWish(Long userId) {
		Wish wish = wishRepository
			.findMainWish(getUser(userId), LocalDateTime.now())
			.orElse(null);
		return nonNull(wish) ? MainWishResponseDTO.from(wish) : null;
	}

	public Wish getWish(Long id) {
		return wishRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(INVALID_WISH.getMessage()));
	}

	private User getUser(Long id) {
		return userRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(INVALID_USER.getMessage()));
	}

	private LocalDateTime convertToTime(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
		return LocalDateTime.parse(date + " 00:00", formatter);
	}
}