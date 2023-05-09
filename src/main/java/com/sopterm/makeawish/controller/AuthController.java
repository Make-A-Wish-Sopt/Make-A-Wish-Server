package com.sopterm.makeawish.controller;

import static com.sopterm.makeawish.common.message.SuccessMessage.*;

import com.sopterm.makeawish.common.ApiResponse;
import com.sopterm.makeawish.dto.auth.AuthGetTokenResponseDto;
import com.sopterm.makeawish.dto.auth.AuthSignInRequestDto;
import com.sopterm.makeawish.dto.auth.AuthSignInResponseDto;
import com.sopterm.makeawish.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Tag(name = "Auth", description = "인증")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;
    @Operation(summary = "카카오 소셜 로그인",
            description = """
                    kakao 인증 서버에서 발급받은 accessToken header에 request
                    """
    )
    @PostMapping()
    public ResponseEntity<ApiResponse> signIn(
            @RequestHeader(value = "authorization") String clientId,
            @RequestBody AuthSignInRequestDto requestDto
    ) {
        AuthSignInResponseDto responseDto = authService.signIn(requestDto, clientId);
        ApiResponse apiResponse = ApiResponse.success(SUCCESS_SIGN_IN.getMessage(), responseDto);
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "토큰 재발급")
    @PostMapping("/token")
    public ResponseEntity<ApiResponse> getToken(Principal principal)
    {
        Long userId = Long.valueOf(principal.getName());
        AuthGetTokenResponseDto responseDto =  authService.getToken(userId);
        ApiResponse apiResponse = ApiResponse.success(SUCCESS_GET_REFRESH_TOKEN.getMessage(), responseDto);
        return ResponseEntity.ok(apiResponse);
    }
}