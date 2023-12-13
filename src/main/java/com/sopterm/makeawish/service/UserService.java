package com.sopterm.makeawish.service;

import com.popbill.api.AccountCheckService;
import com.popbill.api.PopbillException;
import com.sopterm.makeawish.common.AbuseException;
import com.sopterm.makeawish.domain.abuse.AbuseLog;
import com.sopterm.makeawish.domain.abuse.AbuseUser;
import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.dto.user.UserAccountRequestDTO;
import com.sopterm.makeawish.dto.user.UserAccountResponseDTO;
import com.sopterm.makeawish.dto.user.UserAccountVerifyRequestDTO;
import com.sopterm.makeawish.repository.PresentRepository;
import com.sopterm.makeawish.repository.UserRepository;
import com.sopterm.makeawish.repository.abuse.AbuseLogRepository;
import com.sopterm.makeawish.repository.abuse.AbuseUserRepository;
import com.sopterm.makeawish.repository.wish.WishRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sopterm.makeawish.common.message.ErrorMessage.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final WishRepository wishRepository;
    private final PresentRepository presentRepository;
    private final AccountCheckService accountCheckService;
    private final AbuseUserRepository abuseUserRepository;
    private final AbuseLogRepository abuseLogRepository;

    @Value("${popbill.businessNumber}")
    private String corpNum;
    private static final int ABUSE_CAUTION_COUNT = 4;

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
        wisher.updateAccount(
                requestDTO.accountInfo().getName(),
                requestDTO.accountInfo().getBank(),
                requestDTO.accountInfo().getAccount());
        wisher.updatePhoneNumber(requestDTO.phone());
        return UserAccountResponseDTO.of(wisher);
    }

    @Transactional
    public void deleteUser(Long userId) {
        val user = getUser(userId);
        user.getWishes().forEach(wish -> {
            wish.getPresents().forEach(presentRepository::delete);
            wishRepository.delete(wish);
        });
        userRepository.delete(user);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(INVALID_USER.getMessage()));
    }

    @Transactional
    public Integer verifyUserAccount(Long userId, UserAccountVerifyRequestDTO verifyRequestDTO) throws PopbillException {
        checkAbuseUser(userId);
        var response = 0;
        try {
            val accountInfo = accountCheckService.CheckAccountInfo(corpNum, verifyRequestDTO.BankCode(), verifyRequestDTO.AccountNumber());
            if (!verifyRequestDTO.name().equals(accountInfo.getAccountName())) {
                val abuseLog = AbuseLog.builder()
                        .user(getUser(userId))
                        .build();
                abuseLogRepository.save(abuseLog);
                response = countAbuseLogByUser(userId);
            }
        } catch (PopbillException e) {
            throw new PopbillException(e.getCode(), e.getMessage());
        }
        return response;
    }

    public void checkAbuseUser(Long userId) {
        abuseUserRepository.findAbuseUserByUserId(userId)
                .ifPresent(abuseUser -> {
                    throw new AbuseException(IS_ABUSE_USER.getMessage());
                });
    }

    @Transactional
    public Integer countAbuseLogByUser(Long userId) {
        val abuseLogCount = abuseLogRepository.countAbuseLogByUserIdDuringWeekend(userId);
        if (abuseLogCount >= ABUSE_CAUTION_COUNT) {
            createAbuseUser(userId);
        }
        return abuseLogCount;
    }

    @Transactional
    public void createAbuseUser(Long userId) {
        abuseUserRepository.save(new AbuseUser(getUser(userId)));
    }
}
