package com.sopterm.makeawish.dto.auth;

import com.sopterm.makeawish.domain.user.SocialType;
import com.sopterm.makeawish.domain.user.User;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record AuthSignInRequestDto (
        @NonNull SocialType socialType
){
    public static AuthSignInRequestDto from(User user) {
        return AuthSignInRequestDto.builder().socialType(user.getSocialType()).build();
    }

    public User toEntity() {
        return User.builder()
                .socialType(this.socialType)
                .build();
    }
}
