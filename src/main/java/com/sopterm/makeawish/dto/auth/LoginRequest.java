package com.sopterm.makeawish.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class LoginRequest {

    @NotBlank
    private final String socialToken;
    private String nickname;
    @Email
    private String email;

    public LoginRequest(String socialToken, String nickname, @Nullable String email) {
        this.socialToken = socialToken;
        this.nickname = nickname;
        this.email = email;
    }
}
