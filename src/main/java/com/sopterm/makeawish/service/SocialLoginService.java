package com.sopterm.makeawish.service;

import com.sopterm.makeawish.dto.auth.AuthSignInResponseDto;

@FunctionalInterface
public interface SocialLoginService {

    AuthSignInResponseDto socialLogin(String code);
}