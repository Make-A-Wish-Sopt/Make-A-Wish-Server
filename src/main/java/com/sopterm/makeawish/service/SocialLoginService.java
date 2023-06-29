package com.sopterm.makeawish.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sopterm.makeawish.dto.auth.AuthSignInResponseDto;

@FunctionalInterface
public interface SocialLoginService {

    AuthSignInResponseDto socialLogin(String code) throws JsonProcessingException;
}