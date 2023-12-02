package com.sopterm.makeawish.config.crypto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


@Slf4j
@Component
public class KeyEncrypt {
    private static final String ALGORITHM = "AES";
    private static final Charset ENCODING_TYPE = StandardCharsets.UTF_8;
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private final SecretKeySpec secretKeySpec;
    private final IvParameterSpec ivParameterSpec;
    private final Cipher cipher;

    public KeyEncrypt(@Value("${crypto.secret-key}") String secretKey, @Value("${crypto.iv}") String iv) {
        try {
            this.secretKeySpec = new SecretKeySpec(secretKey.getBytes(ENCODING_TYPE), ALGORITHM);
            this.ivParameterSpec = new IvParameterSpec(iv.getBytes());
            this.cipher = Cipher.getInstance(TRANSFORMATION);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            log.error("error: Initializing KeyEncrypt ", e);
            throw new RuntimeException("error: Initializing KeyEncrypt ", e);
        }
    }

    public String encrypt(String plainText) {
        try {
            this.cipher.init(Cipher.ENCRYPT_MODE, this.secretKeySpec, this.ivParameterSpec);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(ENCODING_TYPE));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException |
                 InvalidKeyException e) {
            throw new RuntimeException("error: Encrypting text ", e);
        }
    }

    public String decrypt(String encryptedText) {
        try {
            this.cipher.init(Cipher.DECRYPT_MODE, this.secretKeySpec, this.ivParameterSpec);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
            return new String(decryptedBytes, ENCODING_TYPE);
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException |
                 InvalidKeyException e) {
            throw new RuntimeException("error: Decrypting text ", e);
        }
    }
}
