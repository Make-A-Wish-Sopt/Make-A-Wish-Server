package com.sopterm.makeawish.controller;

import com.sopterm.makeawish.common.ApiResponse;
import com.sopterm.makeawish.domain.user.InternalMemberDetails;
import com.sopterm.makeawish.dto.wish.WishRequestDTO;
import com.sopterm.makeawish.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static com.sopterm.makeawish.common.message.SuccessMessage.*;
import static java.util.Objects.nonNull;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wishes")
@SecurityRequirement(name = "Authorization")
public class WishController {

	private final WishService wishService;

	@Operation(summary = "소원 링크 생성")
	@PostMapping
	public ResponseEntity<ApiResponse> createWish(
		@Parameter(hidden = true) @AuthenticationPrincipal InternalMemberDetails memberDetails,
		@RequestBody WishRequestDTO requestDTO
	) {
		val wishId = wishService.createWish(memberDetails.getId(), requestDTO);
		return ResponseEntity
			.created(getURI(wishId))
			.body(ApiResponse.success(SUCCESS_CREATE_WISH.getMessage(), wishId));
	}

	@Operation(summary = "메인 화면 조회")
	@GetMapping("/main")
	public ResponseEntity<ApiResponse> findMainWish(
		@Parameter(hidden = true) @AuthenticationPrincipal InternalMemberDetails memberDetails
	) {
		val response = wishService.findMainWish(memberDetails.getId());
		return nonNull(response)
			? ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_MAIN_WISH.getMessage(), response))
			: ResponseEntity.status(NO_CONTENT).body(ApiResponse.success(NO_WISH.getMessage()));
	}

	@Operation(summary = "선물 링크 정보 조회")
	@GetMapping("/present/info")
	public ResponseEntity<ApiResponse> getPresentInfo(
		@RequestParam String url,
		@RequestParam String tag
	) throws Exception {
		val response = wishService.getPresentInfo(url, tag);
		return ResponseEntity.ok(ApiResponse.success(SUCCESS_PARSE_HTML.getMessage(), response));
	}

	private URI getURI(Long id) {
		return ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(id)
			.toUri();
	}
}
