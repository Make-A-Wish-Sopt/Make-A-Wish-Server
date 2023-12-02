package com.sopterm.makeawish.config.crypto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-test.yml")
class KeyEncryptTest {
    @Autowired
    KeyEncrypt keyEncryptService;

    @Test
    @DisplayName("암호화/복호화 성공 테스트")
    void 암호화_복호화_성공() throws Exception {
        String plainAccountText = "1234567890";

        String encryptedText = keyEncryptService.encrypt(plainAccountText);
        String decryptedText = keyEncryptService.decrypt(encryptedText);

        Assertions.assertEquals(plainAccountText, decryptedText);
    }
}