package com.sopterm.makeawish.service;

import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.dto.user.UserAccountResponseDTO;
import com.sopterm.makeawish.dto.user.UserAccountUpdateRequestDTO;
import com.sopterm.makeawish.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.sopterm.makeawish.common.message.ErrorMessage.INVALID_USER;
import static com.sopterm.makeawish.common.message.ErrorMessage.NO_EXIST_USER_ACCOUNT;
import static java.util.Objects.nonNull;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserAccountResponseDTO getUserAccount(Long userId) {
        val wisher = getUser(userId);
        return nonNull(wisher.getAccount()) ? UserAccountResponseDTO.from(wisher) : null;
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(INVALID_USER.getMessage()));
    }

    public UserAccountResponseDTO updateUserAccount(Long userId, UserAccountUpdateRequestDTO requestDTO) {
        val wisher = getUser(userId);
        if(Objects.isNull(requestDTO.accountInfo())) {
            throw new IllegalArgumentException(NO_EXIST_USER_ACCOUNT.getMessage());
        }
        wisher.updateAccount(
            requestDTO.accountInfo().getName(),
            requestDTO.accountInfo().getBank(),
            requestDTO.accountInfo().getAccount());
        return UserAccountResponseDTO.from(wisher);
    }
}