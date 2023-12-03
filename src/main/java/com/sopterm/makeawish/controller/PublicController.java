package com.sopterm.makeawish.controller;

import static com.sopterm.makeawish.common.message.SuccessMessage.*;

import java.nio.file.AccessDeniedException;

import com.sopterm.makeawish.dto.cake.CakeCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sopterm.makeawish.common.ApiResponse;
import com.sopterm.makeawish.dto.cake.CakeApproveRequestDTO;
import com.sopterm.makeawish.dto.cake.CakeReadyRequestDTO;
import com.sopterm.makeawish.service.CakeService;
import com.sopterm.makeawish.service.WishService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/public")
public class PublicController {

	private final CakeService cakeService;
	private final WishService wishService;

	@Operation(summary = "케이크 리스트 조회")
	@GetMapping("/cakes")
	public ResponseEntity<ApiResponse> getAllCakes() {
		val response = cakeService.getAllCakes();
		return ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_ALL_CAKE.getMessage(), response));
	}

	@Operation(summary = "소원 링크 조회")
	@GetMapping("/wishes/{wishId}")
	public ResponseEntity<ApiResponse> findWish(@PathVariable Long wishId) throws AccessDeniedException {
		val response = wishService.findWish(wishId);
		return ResponseEntity.ok(ApiResponse.success(SUCCESS_FIND_WISH.getMessage(), response));
	}

	@Operation(summary = "카카오페이 결제 준비")
	@PostMapping("/pay/ready")
	public ResponseEntity<ApiResponse> getKakaoPayReady(@RequestBody CakeReadyRequestDTO request) {
		val response = cakeService.getKakaoPayReady(request);
		return ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_READY_KAKAOPAY.getMessage(), response));
	}

	@Operation(summary = "카카오페이 결제 승인 및 선물 저장")
	@PostMapping("/pay/approve")
	public ResponseEntity<ApiResponse> createCake(@RequestBody CakeApproveRequestDTO request) {
		val cake = cakeService.getCake(request.cakeId());
		if (cake.getId() != 1) {
			cakeService.getKakaoPayApprove(request);
		}
		val wish = wishService.getWish(request.wishId());
		val response = cakeService.createPresent(request.name(), cake, wish, request.message());
		return ResponseEntity.ok(ApiResponse.success(SUCCESS_CREATE_CAKE.getMessage(), response));
	}

    @Operation(summary = "케이크 저장하기")
    @PostMapping("/cakes")
    public ResponseEntity<ApiResponse> createCakePresent(@RequestBody CakeCreateRequest request) {
        val response = cakeService.createPresentNew(request);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS_CREATE_CAKE.getMessage(), response));
    }
}
