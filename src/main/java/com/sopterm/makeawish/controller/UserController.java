package com.sopterm.makeawish.controller;

import com.sopterm.makeawish.common.ApiResponse;
import com.sopterm.makeawish.domain.user.InternalMemberDetails;
import com.sopterm.makeawish.dto.user.UserAccountUpdateRequestDTO;
import com.sopterm.makeawish.dto.user.UserWishUpdateRequestDTO;
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

import static com.sopterm.makeawish.common.message.ErrorMessage.EXPIRED_BIRTHDAY_WISH;
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

    @Operation(summary = "진행 중인 소원 수정", description = "수정되지 않은 정보는 null 또는 원래의 정보 그대로 전달")
    @PutMapping("/wish")
    public ResponseEntity<ApiResponse> updateUserMainWish(
        @Parameter(hidden = true) @AuthenticationPrincipal InternalMemberDetails memberDetails,
        @RequestBody UserWishUpdateRequestDTO requestDTO
    ) {
        val wish = userService.updateUserMainWish(memberDetails.getId(), requestDTO);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS_UPDATE_USER_INFO.getMessage(), wish));
    }

    @Operation(summary = "진행 중인 소원 정보 조회")
    @GetMapping("/wish")
    public ResponseEntity<ApiResponse> findUserMainWish(
        @Parameter(hidden = true) @AuthenticationPrincipal InternalMemberDetails memberDetails
    ) {
        val response = userService.findUserMainWish(memberDetails.getId());
        return nonNull(response)
                ? ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_USER_INFO.getMessage(), response))
                : ResponseEntity.ok(ApiResponse.fail(EXPIRED_BIRTHDAY_WISH.getMessage()));
    }

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
            @RequestBody UserAccountUpdateRequestDTO requestDTO
    ) {
        val response = userService.updateUserAccount(memberDetails.getId(), requestDTO);
        return ResponseEntity
                .ok(ApiResponse.success(SUCCESS_UPDATE_USER_ACCOUNT_INFO.getMessage(), response));
    }
}
