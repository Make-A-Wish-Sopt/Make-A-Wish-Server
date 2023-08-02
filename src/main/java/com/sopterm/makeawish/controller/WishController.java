package com.sopterm.makeawish.controller;

import com.sopterm.makeawish.common.ApiResponse;
import com.sopterm.makeawish.domain.user.InternalMemberDetails;
import com.sopterm.makeawish.dto.wish.WishIdRequestDTO;
import com.sopterm.makeawish.dto.wish.WishRequestDTO;
import com.sopterm.makeawish.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.nio.file.AccessDeniedException;

import static com.sopterm.makeawish.common.ApiResponse.*;
import static com.sopterm.makeawish.common.message.SuccessMessage.*;
import static java.util.Objects.nonNull;
import static org.springframework.http.HttpStatus.*;

@Tag(name = "Wish", description = "유저 소원 API")
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
			.body(success(SUCCESS_CREATE_WISH.getMessage(), wishId));
	}

	@Operation(summary = "메인 화면 조회")
	@GetMapping("/main")
	public ResponseEntity<ApiResponse> findMainWish(
		@Parameter(hidden = true) @AuthenticationPrincipal InternalMemberDetails memberDetails
	) {
		val response = wishService.findMainWish(memberDetails.getId());
		return nonNull(response)
			? ResponseEntity.ok(success(SUCCESS_GET_MAIN_WISH.getMessage(), response))
			: ResponseEntity.status(NO_CONTENT).body(success(NO_WISH.getMessage()));
	}

	@Operation(summary = "선물 링크 정보 조회")
	@GetMapping("/present/info")
	public ResponseEntity<ApiResponse> getPresentInfo(
		@RequestParam String url,
		@RequestParam String tag
	) throws Exception {
		val response = wishService.getPresentInfo(url, tag);
		return ResponseEntity.ok(success(SUCCESS_PARSE_HTML.getMessage(), response));
	}

	@Operation(summary = "소원 단건 조회")
	@GetMapping("/{wishId}")
	public ResponseEntity<ApiResponse> findWish(
		@Parameter(hidden = true) @AuthenticationPrincipal InternalMemberDetails memberDetails,
		@PathVariable Long wishId
	) throws AccessDeniedException {
		val response = wishService.findWish(memberDetails.getId(), wishId);
		return ResponseEntity.ok(success(SUCCESS_GET_WISH.getMessage(), response));
	}

	@Operation(summary = "소원 모아보기")
	@GetMapping
	public ResponseEntity<ApiResponse> findWishes(
		@Parameter(hidden = true) @AuthenticationPrincipal InternalMemberDetails memberDetails
	) {
		val response = wishService.findWishes(memberDetails.getId());
		return ResponseEntity.ok(success(SUCCESS_GET_WISHES.getMessage(), response));
	}

	@Operation(summary = "소원 삭제")
	@DeleteMapping
	public ResponseEntity<ApiResponse> deleteWishes(@RequestBody WishIdRequestDTO requestDTO) {
		wishService.deleteWishes(requestDTO);
		return ResponseEntity.ok(success(SUCCESS_DELETE_WISHES.getMessage()));
	}

	private URI getURI(Long id) {
		return ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(id)
			.toUri();
	}
}
