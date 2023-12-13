package com.sopterm.makeawish.service;

import com.popbill.api.AccountCheckService;
import com.popbill.api.PopbillException;
import com.sopterm.makeawish.dto.user.UserAccountVerifyRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {
    @Autowired
    UserService userService;

    @Autowired
    AccountCheckService accountCheckService;

    @Test
    @DisplayName("유효하지 않은 기관코드인 경우 예외 발생")
    void 계좌_연동_실패() throws PopbillException {
        // given
        String corpNum = "0123456789";
        UserAccountVerifyRequestDTO requestDTO = UserAccountVerifyRequestDTO.builder()
                .BankCode("0000")
                .name("홍길동")
                .AccountNumber("1234567890")
                .build();
        assertThrows(PopbillException.class, () -> {
            accountCheckService.CheckAccountInfo(corpNum, requestDTO.BankCode(), requestDTO.AccountNumber());
        });
    }
}