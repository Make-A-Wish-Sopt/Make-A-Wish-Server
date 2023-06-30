package com.sopterm.makeawish.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sopterm.makeawish.common.ApiResponse;
import com.sopterm.makeawish.domain.user.InternalMemberDetails;
import com.sopterm.makeawish.dto.auth.AuthGetTokenResponseDto;
import com.sopterm.makeawish.dto.auth.AuthSignInResponseDto;
import com.sopterm.makeawish.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.sopterm.makeawish.common.message.SuccessMessage.SUCCESS_GET_REFRESH_TOKEN;
import static com.sopterm.makeawish.common.message.SuccessMessage.SUCCESS_SIGN_IN;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "인증")
public class AuthController {

    private final AuthService authService;
    @Operation(summary = "카카오 소셜 로그인",
            description = """
                    kakao 인증 서버에서 발급받은 accessToken header에 request
                    """
    )
    @PostMapping("/kakao/callback")
    public ResponseEntity<ApiResponse> signIn(@RequestParam String code) throws JsonProcessingException {
        AuthSignInResponseDto responseDto = authService.socialLogin("KAKAO", code);
        ApiResponse apiResponse = ApiResponse.success(SUCCESS_SIGN_IN.getMessage(), responseDto);
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "AccessToken 재발급")
    @PostMapping("/token")
    public ResponseEntity<ApiResponse> getToken(@Parameter(hidden = true) @AuthenticationPrincipal InternalMemberDetails memberDetails)
    {
        System.out.println(memberDetails.getId());
        AuthGetTokenResponseDto responseDto =  authService.getToken(memberDetails.getId());
        ApiResponse apiResponse = ApiResponse.success(SUCCESS_GET_REFRESH_TOKEN.getMessage(), responseDto);
        return ResponseEntity.ok(apiResponse);
    }
}