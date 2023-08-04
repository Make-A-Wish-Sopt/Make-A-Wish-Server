package com.sopterm.makeawish.service;

import static com.sopterm.makeawish.common.Util.*;
import static com.sopterm.makeawish.common.message.ErrorMessage.*;
import static com.sopterm.makeawish.domain.wish.WishStatus.*;
import static java.util.Objects.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.domain.wish.Wish;
import com.sopterm.makeawish.dto.user.UserAccountResponseDTO;
import com.sopterm.makeawish.dto.user.UserAccountUpdateRequestDTO;
import com.sopterm.makeawish.dto.user.UserWishUpdateResponseDTO;
import com.sopterm.makeawish.dto.user.UserWishUpdateRequestDTO;
import com.sopterm.makeawish.repository.UserRepository;
import com.sopterm.makeawish.repository.wish.WishRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.val;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

	private final UserRepository userRepository;
	private final WishRepository wishRepository;

	@Transactional
	public UserWishUpdateResponseDTO updateUserMainWish(Long userId, UserWishUpdateRequestDTO request) {
		val wisher = getUser(userId);
		val wish =  getUserMainWish(wisher);

		val status = wish.getStatus();
		if (status.equals(END)) {
			throw new IllegalArgumentException(NOT_CURRENT_WISH.getMessage());
		}
		if (status.equals(BEFORE)) {
			val startDate = nonNull(request.startDate()) ? convertToTime(request.startDate()) : null;
			val endDate = nonNull(request.endDate()) ? convertToTime(request.endDate()) : null;
			wish.updateTerm(startDate, endDate);
		}
		if (status.equals(BEFORE) || status.equals(WHILE)) {
			wisher.updateProfile(request.name(), request.bankName(), request.account(), request.phone());
		}

		return UserWishUpdateResponseDTO.of(wisher, wish);
	}

	public UserWishUpdateResponseDTO findUserMainWish(Long userId) {
		val wisher = getUser(userId);
		val wish =  getUserMainWish(wisher);
		return UserWishUpdateResponseDTO.of(wisher, wish);
	}

	public UserAccountResponseDTO getUserAccount(Long userId) {
		val wisher = getUser(userId);
		return nonNull(wisher.getAccount()) ? UserAccountResponseDTO.of(wisher) : null;
	}

	@Transactional
	public UserAccountResponseDTO updateUserAccount(Long userId, UserAccountUpdateRequestDTO requestDTO) {
		val wisher = getUser(userId);
		if (isNull(requestDTO.accountInfo())) {
			throw new IllegalArgumentException(NO_EXIST_USER_ACCOUNT.getMessage());
		}
		wisher.updateAccount(
			requestDTO.accountInfo().getName(),
			requestDTO.accountInfo().getBank(),
			requestDTO.accountInfo().getAccount());
		return UserAccountResponseDTO.of(wisher);
	}

	private User getUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new EntityNotFoundException(INVALID_USER.getMessage()));
	}

	private Wish getUserMainWish(User user) {
		return   wishRepository
			.findMainWish(user, 0)
			.orElseThrow(() -> new EntityNotFoundException(NO_WISH.getMessage()));
	}
}
