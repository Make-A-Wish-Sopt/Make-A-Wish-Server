package com.sopterm.makeawish.controller;

import static com.sopterm.makeawish.common.message.SuccessMessage.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
