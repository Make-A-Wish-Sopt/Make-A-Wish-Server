package com.sopterm.makeawish.service;

import static com.sopterm.makeawish.common.message.ErrorMessage.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.domain.wish.Wish;
import com.sopterm.makeawish.dto.wish.WishRequestDTO;
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
		Wish wish = requestDTO.toEntity(getUser(userId));
		return wishRepository.save(wish).getId();
	}

	private User getUser(Long id) {
		return userRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(INVALID_USER.getMessage()));
	}
}
