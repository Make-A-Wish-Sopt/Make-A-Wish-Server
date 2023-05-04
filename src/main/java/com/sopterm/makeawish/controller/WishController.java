package com.sopterm.makeawish.controller;

import static com.sopterm.makeawish.common.message.ErrorMessage.*;
import static com.sopterm.makeawish.common.message.SuccessMessage.*;
import static java.util.Objects.*;

import java.net.URI;
import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.sopterm.makeawish.common.ApiResponse;
import com.sopterm.makeawish.dto.wish.WishRequestDTO;
import com.sopterm.makeawish.dto.wish.WishResponseDTO;
import com.sopterm.makeawish.service.WishService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wishes")
public class WishController {

	private final WishService wishService;

	@Operation(description = "소원 링크 생성")
	@PostMapping
	public ResponseEntity<ApiResponse> createWish(Principal principal, @RequestBody WishRequestDTO requestDTO) {
		Long wishId = wishService.createWish(getUserId(principal), requestDTO);
		return ResponseEntity
			.created(getURI(wishId))
			.body(ApiResponse.success(SUCCESS_CREATE_WISH.getMessage()));
	}

	@Operation(description = "소원 링크 조회")
	@GetMapping("/{wishId}")
	public ResponseEntity<ApiResponse> findWish(@PathVariable Long wishId) {
		WishResponseDTO response = wishService.findWish(wishId);
		return ResponseEntity.ok(ApiResponse.success(SUCCESS_FIND_WISH.getMessage(), response));
	}

	private Long getUserId(Principal principal) {
		if (isNull(principal)) {
			throw new IllegalArgumentException(NULL_PRINCIPAL.getMessage());
		}
		return Long.valueOf(principal.getName());
	}

	private URI getURI(Long id) {
		return ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(id)
			.toUri();
	}
}
