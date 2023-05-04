package com.sopterm.makeawish.controller;

import static com.sopterm.makeawish.common.message.SuccessMessage.*;

import java.util.List;

import com.sopterm.makeawish.dto.cake.CakeReadyRequestDto;
import com.sopterm.makeawish.dto.cake.CakeReadyResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sopterm.makeawish.common.ApiResponse;
import com.sopterm.makeawish.dto.cake.CakeResponseDTO;
import com.sopterm.makeawish.service.CakeService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cakes")
public class CakeController {

	private final CakeService cakeService;

	@Operation(description = "케이크 리스트 조회")
	@GetMapping
	public ResponseEntity<ApiResponse> getAllCakes() {
		List<CakeResponseDTO> response = cakeService.getAllCakes();
		return ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_ALL_CAKE.getMessage(), response));
	}

	@PostMapping("/pay/ready")
	public ResponseEntity<ApiResponse> getKakaoPayReady(@RequestBody CakeReadyRequestDto request){
		CakeReadyResponseDto response=cakeService.getKakaoPayReady(request);
		return ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_READY_KAKAOPAY.getMessage(), response));
	}
}
