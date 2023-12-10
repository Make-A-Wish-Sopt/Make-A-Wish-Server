package com.sopterm.makeawish.service;

import com.popbill.api.AccountCheckInfo;
import com.popbill.api.AccountCheckService;
import com.popbill.api.PopbillException;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


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
    void 계좌_연동_실패(){
        // given
        String BankCode = "0000";
        String AccountNumber = "1234567890";

        // when - then
        assertThatThrownBy(() -> userService.verifyUserAccount("홍길동",BankCode, AccountNumber))
                .isInstanceOf(PopbillException.class);
    }
}