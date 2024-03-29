package com.sopterm.makeawish.controller;

import com.sopterm.makeawish.common.ApiResponse;
import com.sopterm.makeawish.domain.user.InternalMemberDetails;
import com.sopterm.makeawish.dto.user.UserAccountRequestDTO;
import com.sopterm.makeawish.dto.user.UserAccountVerifyRequestDTO;
import com.sopterm.makeawish.service.AbuseService;
import com.sopterm.makeawish.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.sopterm.makeawish.common.message.ErrorMessage.NOT_VALID_USER_ACCOUNT;
import static com.sopterm.makeawish.common.message.ErrorMessage.NO_EXIST_USER_ACCOUNT;
import static com.sopterm.makeawish.common.message.SuccessMessage.*;
import static java.util.Objects.nonNull;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@SecurityRequirement(name = "Authorization")
@Tag(name = "User", description = "마이페이지")
public class UserController {

    private final UserService userService;
    private final AbuseService abuseService;
    private static final int VERIFY_ACCOUNT_SUCCESS = 0;

    @Operation(summary = "유저 계좌 정보 가져오기")
    @GetMapping("/account")
    public ResponseEntity<ApiResponse> getUserAccount(
            @Parameter(hidden = true) @AuthenticationPrincipal InternalMemberDetails memberDetails
    ) {
        val response = userService.getUserAccount(memberDetails.getId());
        return nonNull(response)
                ? ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_USER_ACCOUNT_INFO.getMessage(), response))
                : ResponseEntity.ok(ApiResponse.fail(NO_EXIST_USER_ACCOUNT.getMessage()));
    }

    @Operation(summary = "유저 계좌 정보 수정")
    @PutMapping("/account")
    public ResponseEntity<ApiResponse> updateUserAccount(
            @Parameter(hidden = true) @AuthenticationPrincipal InternalMemberDetails memberDetails,
            @RequestBody UserAccountRequestDTO requestDTO
    ) {
        val response = userService.updateUserAccount(memberDetails.getId(), requestDTO);
        return ResponseEntity
                .ok(ApiResponse.success(SUCCESS_UPDATE_USER_ACCOUNT_INFO.getMessage(), response));
    }

    @Operation(summary = "유저 탈퇴")
    @DeleteMapping
    public ResponseEntity<ApiResponse> deleteUser(
            @Parameter(hidden = true) @AuthenticationPrincipal InternalMemberDetails memberDetails
    ) {
        userService.deleteUser(memberDetails.getId());
        return ResponseEntity.ok(ApiResponse.success(SUCCESS_DELETE_USER.getMessage()));
    }

    @Operation(summary = "계좌 실명 조회")
    @PostMapping("/verify-account")
    public ResponseEntity<ApiResponse> checkAccountInformation(
            @Parameter(hidden = true) @AuthenticationPrincipal InternalMemberDetails memberDetails,
            @RequestBody UserAccountVerifyRequestDTO verifyRequestDTO) throws Exception {
        val response = userService.verifyUserAccount(memberDetails.getId(), verifyRequestDTO);
        return response == VERIFY_ACCOUNT_SUCCESS
                ? ResponseEntity.ok(ApiResponse.success(SUCCESS_VERIFY_USER_ACCOUNT.getMessage()))
                : ResponseEntity.ok(ApiResponse.fail(NOT_VALID_USER_ACCOUNT.getMessage(), response));
    }

    @Operation(summary = "어뷰징 유저 확인")
    @GetMapping("/abuse")
    public ResponseEntity<ApiResponse> checkAbuseUser(
            @Parameter(hidden = true) @AuthenticationPrincipal InternalMemberDetails memberDetails
    ) {
        abuseService.checkAbuseUser(memberDetails.getId());
        val response = abuseService.countAbuseLogByUser(memberDetails.getId());
        return ResponseEntity.ok(ApiResponse.success(IS_NOT_ABUSE_USER.getMessage(), response));
    }
}
