package com.sopterm.makeawish.service;

import com.popbill.api.AccountCheckInfo;
import com.popbill.api.AccountCheckService;
import com.popbill.api.PopbillException;
import com.sopterm.makeawish.domain.abuse.AbuseLog;
import com.sopterm.makeawish.domain.user.TransferInfo;
import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.dto.user.UserAccountRequestDTO;
import com.sopterm.makeawish.dto.user.UserAccountResponseDTO;
import com.sopterm.makeawish.dto.user.UserAccountVerifyRequestDTO;
import com.sopterm.makeawish.repository.PresentRepository;
import com.sopterm.makeawish.repository.UserRepository;
import com.sopterm.makeawish.repository.wish.WishRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.sopterm.makeawish.common.message.ErrorMessage.INVALID_USER;
import static com.sopterm.makeawish.common.message.ErrorMessage.NO_EXIST_USER_ACCOUNT;
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
    private final AbuseService abuseService;


    @Value("${popbill.businessNumber}")
    private String corpNum;
    private static final List<String> USER_FAIL_CODE = List.of("300", "301", "400", "801", "898", "899");

    public UserAccountResponseDTO getUserAccount(Long userId) {
        val wisher = getUser(userId);
        return nonNull(wisher.getTransferInfo().getAccountInfo()) || nonNull(wisher.getTransferInfo().getKakaoPayCode()) ? UserAccountResponseDTO.of(wisher) : null;
    }

    @Transactional
    public UserAccountResponseDTO updateUserAccount(Long userId, UserAccountRequestDTO requestDTO) {
        val wisher = getUser(userId);
        if (isNull(requestDTO.kakaoPayCode()) && isNull(requestDTO.accountInfo())) {
            throw new IllegalArgumentException(NO_EXIST_USER_ACCOUNT.getMessage());
        }
        wisher.updateTransferInfo(new TransferInfo(requestDTO));
        return UserAccountResponseDTO.of(wisher);
    }

    @Transactional
    public void deleteUser(Long userId) {
        val user = getUser(userId);
        user.getWishes().forEach(wish -> {
            wish.getPresents().forEach(presentRepository::delete);
            wishRepository.delete(wish);
        });
        abuseService.deleteAbuseLogByUser(user);
        abuseService.deleteAbuseUserByUser(user);
        userRepository.delete(user);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(INVALID_USER.getMessage()));
    }

    @Transactional
    public AccountCheckInfo verifyUserAccount(Long userId, UserAccountVerifyRequestDTO verifyRequestDTO) throws PopbillException {
        abuseService.checkAbuseUser(userId);
        val accountInfo = accountCheckService.CheckAccountInfo(corpNum, verifyRequestDTO.BankCode(), verifyRequestDTO.AccountNumber());
        if (checkIsUserFail(accountInfo.getResult())) {
            val abuseLog = AbuseLog.builder()
                    .user(getUser(userId))
                    .build();
            abuseService.createAbuseLog(abuseLog);
        }
        return accountInfo;
    }

    private boolean checkIsUserFail(String result) {
        return USER_FAIL_CODE.contains(result);
    }
}
