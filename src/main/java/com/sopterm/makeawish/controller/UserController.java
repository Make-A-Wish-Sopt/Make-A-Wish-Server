package com.sopterm.makeawish.controller;

import com.sopterm.makeawish.common.ApiResponse;
import com.sopterm.makeawish.domain.user.InternalMemberDetails;
import com.sopterm.makeawish.dto.wish.UserWishUpdateRequestDTO;
import com.sopterm.makeawish.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.sopterm.makeawish.common.message.ErrorMessage.*;
import static com.sopterm.makeawish.common.message.SuccessMessage.*;
import static java.util.Objects.nonNull;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@SecurityRequirement(name = "Authorization")
@Tag(name = "User", description = "마이페이지")
public class UserController {

    private final WishService wishService;
    @Operation(summary = "진행중인 소원 수정", description = "수정되지 않은 정보는 null 또는 원래의 정보 그대로 request로 전달부탁드립니다!")
    @PutMapping("/wish")
    public ResponseEntity<ApiResponse> updateUserMainWish(
        @Parameter(hidden = true) @AuthenticationPrincipal InternalMemberDetails memberDetails,
        @RequestBody UserWishUpdateRequestDTO requestDTO
    ) {
        val wish = wishService.updateUserMainWish(memberDetails.getId(), requestDTO);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS_UPDATE_USER_INFO.getMessage(), wish));
    }

    @Operation(summary = "진행중인 소원 가져오기")
    @GetMapping("/wish")
    public ResponseEntity<ApiResponse> getUserMainWish(
        @Parameter(hidden = true) @AuthenticationPrincipal InternalMemberDetails memberDetails
    ) {
        val response = wishService.getUserMainWish(memberDetails.getId());
        return nonNull(response)
                ? ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_USER_INFO.getMessage(), response))
                : ResponseEntity.ok(ApiResponse.fail(EXPIRED_BIRTHDAY_WISH.getMessage()));
    }
}
