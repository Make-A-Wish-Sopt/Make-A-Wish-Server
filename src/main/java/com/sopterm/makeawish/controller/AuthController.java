package com.sopterm.makeawish.controller;

import com.sopterm.makeawish.common.ApiResponse;
import com.sopterm.makeawish.dto.auth.AuthGetTokenResponseDto;
import com.sopterm.makeawish.dto.auth.AuthSignInRequestDto;
import com.sopterm.makeawish.dto.auth.AuthSignInResponseDto;
import com.sopterm.makeawish.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
@Tag(name = "Auth", description = "인증")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;
    @PostMapping()
    public ResponseEntity<ApiResponse> signIn(
            @RequestHeader(value = "authorization") String clientId,
            @RequestBody AuthSignInRequestDto requestDto
    ) {
        AuthSignInResponseDto responseDto = authService.signIn(requestDto, clientId);
        ApiResponse apiResponse = ApiResponse.of(
                HttpStatus.OK.value(), true, "소셜 로그인성공", responseDto
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/token")
    public ResponseEntity<ApiResponse> getToken(Principal principal)
    {
        Long userId = Long.valueOf(principal.getName());
        AuthGetTokenResponseDto responseDto =  authService.getToken(userId);
        ApiResponse apiResponse = ApiResponse.of(
                HttpStatus.OK.value(), true, "토큰 재발급 성공", responseDto);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}