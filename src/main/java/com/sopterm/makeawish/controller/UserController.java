package com.sopterm.makeawish.controller;

import com.sopterm.makeawish.common.ApiResponse;
import com.sopterm.makeawish.dto.wish.MypageWishUpdateResponseDTO;
import com.sopterm.makeawish.dto.wish.MypageWishUpdateRequestDTO;
import com.sopterm.makeawish.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.sopterm.makeawish.common.message.ErrorMessage.NULL_PRINCIPAL;
import static com.sopterm.makeawish.common.message.SuccessMessage.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Tag(name = "User", description = "마이페이지")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final WishService wishService;
    @Operation(summary = "내 정보 수정",
            description = """
                    수정되지 않은 정보는 null 또는 원래의 정보 그대로 request로 전달부탁드립니다!
                    """)
    @PutMapping
    public ResponseEntity<ApiResponse> updateWish(Principal principal, @RequestBody MypageWishUpdateRequestDTO requestDTO) {
        MypageWishUpdateResponseDTO wish = wishService.updateWish(getUserSocialId(principal), requestDTO);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS_UPDATE_USER_INFO.getMessage(), wish));
    }

    @Operation(summary = "내 정보 가져오기")
    @GetMapping
    public ResponseEntity<ApiResponse> getUserWish(Principal principal) {
        MypageWishUpdateResponseDTO response = wishService.getMypageWish(getUserSocialId(principal));
        return nonNull(response)
                ? ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_USER_INFO.getMessage(), response))
                : ResponseEntity.ok(ApiResponse.fail(NO_WISH.getMessage()));
    }

    private Long getUserSocialId(Principal principal) {
        if (isNull(principal)) {
            throw new IllegalArgumentException(NULL_PRINCIPAL.getMessage());
        }
        return Long.valueOf(principal.getName());
    }
}
