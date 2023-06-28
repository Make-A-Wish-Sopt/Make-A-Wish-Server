package com.sopterm.makeawish.controller;

import com.sopterm.makeawish.common.ApiResponse;
import com.sopterm.makeawish.dto.wish.MainWishResponseDTO;
import com.sopterm.makeawish.dto.wish.WishRequestDTO;
import com.sopterm.makeawish.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;

import static com.sopterm.makeawish.common.message.ErrorMessage.NULL_PRINCIPAL;
import static com.sopterm.makeawish.common.message.SuccessMessage.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wishes")
public class WishController {

	private final WishService wishService;

	@Operation(summary = "소원 링크 생성")
	@PostMapping
	public ResponseEntity<ApiResponse> createWish(Principal principal, @RequestBody WishRequestDTO requestDTO) {
		Long wishId = wishService.createWish(getUserId(principal), requestDTO);
		return ResponseEntity
			.created(getURI(wishId))
			.body(ApiResponse.success(SUCCESS_CREATE_WISH.getMessage(), wishId));
	}

	@Operation(summary = "메인 화면 조회")
	@GetMapping("/main")
	public ResponseEntity<ApiResponse> findMainWish(Principal principal) {
		MainWishResponseDTO response = wishService.findMainWish(getUserId(principal));
		return nonNull(response)
			? ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_MAIN_WISH.getMessage(), response))
			: ResponseEntity.status(NO_CONTENT).body(ApiResponse.success(NO_WISH.getMessage()));
	}

	@Operation(summary = "선물 url 정보 조회")
	@GetMapping("/present/info")
	public ResponseEntity<ApiResponse> getPresentInfo(
		@RequestParam String url, @RequestParam String tag) throws Exception {
		String response = wishService.getPresentInfo(url, tag);
		return ResponseEntity.ok(ApiResponse.success(SUCCESS_PARSE_HTML.getMessage(), response));
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
