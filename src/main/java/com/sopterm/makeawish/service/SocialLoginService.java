package com.sopterm.makeawish.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sopterm.makeawish.dto.auth.AuthSignInResponseDTO;

@FunctionalInterface
public interface SocialLoginService {

    AuthSignInResponseDTO socialLogin(String code) throws JsonProcessingException;
}