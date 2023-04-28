package com.sopterm.makeawish.service;

import static com.sopterm.makeawish.common.message.ErrorMessage.*;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.domain.wish.Wish;
import com.sopterm.makeawish.dto.wish.WishRequestDTO;
import com.sopterm.makeawish.dto.wish.WishResponseDTO;
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

	public WishResponseDTO findWish(Long wishId) {
		return WishResponseDTO.from(getWish(wishId));
	}

	private Wish getWish(Long id) {
		return wishRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(INVALID_WISH.getMessage()));
	}

	private User getUser(Long id) {
		return userRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(INVALID_USER.getMessage()));
	}
}
