package com.sopterm.makeawish.controller;

import com.sopterm.makeawish.common.ApiResponse;
import com.sopterm.makeawish.domain.Cake;
import com.sopterm.makeawish.domain.wish.Wish;
import com.sopterm.makeawish.dto.cake.*;
import com.sopterm.makeawish.dto.wish.WishResponseDTO;
import com.sopterm.makeawish.service.CakeService;
import com.sopterm.makeawish.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sopterm.makeawish.common.message.SuccessMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/presents")
@Tag(name = "Presents", description = "유저의 친구들이 사용하는 API")
public class PresentController {

    private final CakeService cakeService;
    private final WishService wishService;

    @Operation(summary = "케이크 리스트 조회")
    @GetMapping("/cakes")
    public ResponseEntity<ApiResponse> getAllCakes() {
        List<CakeResponseDTO> response = cakeService.getAllCakes();
        return ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_ALL_CAKE.getMessage(), response));
    }

    @Operation(summary = "친구 소원 링크 조회")
    @GetMapping("/wish/{wishId}")
    public ResponseEntity<ApiResponse> findWish(@PathVariable Long wishId) {
        WishResponseDTO response = wishService.findWish(wishId);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS_FIND_WISH.getMessage(), response));
    }

    @Operation(summary = "카카오페이 결제 준비")
    @PostMapping("/pay/ready")
    public ResponseEntity<ApiResponse> getKakaoPayReady(@RequestBody CakeReadyRequestDto request) {
        CakeReadyResponseDto response = cakeService.getKakaoPayReady(request);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_READY_KAKAOPAY.getMessage(), response));
    }

    @Operation(summary = "카카오페이 결제 승인 및 선물 저장")
    @PostMapping("/pay/approve")
    public ResponseEntity<ApiResponse> createCake(@RequestBody CakeApproveRequestDto request) {
        Cake cake = cakeService.findById(request.cake());
        if (cake.getId() != 1) {
            CakeApproveResponseDto response = cakeService.getKakaoPayApprove(request);
        }
        Wish wish = wishService.getWish(request.wishId());
        CakeCreateResponseDto response = cakeService.createPresent(request.name(), cake, wish, request.message());
        return ResponseEntity.ok(ApiResponse.success(SUCCESS_CREATE_CAKE.getMessage(), response));
    }
}
