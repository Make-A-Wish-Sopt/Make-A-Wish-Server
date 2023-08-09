package com.sopterm.makeawish.service;

import static com.sopterm.makeawish.common.message.ErrorMessage.*;
import static java.util.Objects.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.dto.user.UserAccountResponseDTO;
import com.sopterm.makeawish.dto.user.UserAccountRequestDTO;
import com.sopterm.makeawish.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.val;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

	private final UserRepository userRepository;

	public UserAccountResponseDTO getUserAccount(Long userId) {
		val wisher = getUser(userId);
		return nonNull(wisher.getAccount()) ? UserAccountResponseDTO.of(wisher) : null;
	}

	@Transactional
	public UserAccountResponseDTO updateUserAccount(Long userId, UserAccountRequestDTO requestDTO) {
		val wisher = getUser(userId);
		if (isNull(requestDTO.accountInfo())) {
			throw new IllegalArgumentException(NO_EXIST_USER_ACCOUNT.getMessage());
		}
		wisher.updateAccount(requestDTO.accountInfo().getName(),
			requestDTO.accountInfo().getBank(),
			requestDTO.accountInfo().getAccount());
		wisher.updatePhoneNumber(requestDTO.phone());
		return UserAccountResponseDTO.of(wisher);
	}

	private User getUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new EntityNotFoundException(INVALID_USER.getMessage()));
	}
}
