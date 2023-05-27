package com.sopterm.makeawish.dto.auth;

import com.sopterm.makeawish.domain.user.SocialType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SignupRequest {

    private final String email;
    private final SocialType socialType;
    private final String SocialId;
    private final String nickname;
    private final LocalDateTime createdAt;
}
