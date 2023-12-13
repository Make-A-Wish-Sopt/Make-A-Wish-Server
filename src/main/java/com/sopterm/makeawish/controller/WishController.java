package com.sopterm.makeawish.controller;

import com.sopterm.makeawish.common.ApiResponse;
import com.sopterm.makeawish.domain.user.InternalMemberDetails;
import com.sopterm.makeawish.dto.wish.UserWishUpdateRequestDTO;
import com.sopterm.makeawish.dto.wish.WishIdRequestDTO;
import com.sopterm.makeawish.dto.wish.WishRequestDTO;
import com.sopterm.makeawish.service.UserService;
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
import static com.sopterm.makeawish.common.message.ErrorMessage.*;
import static com.sopterm.makeawish.common.message.ErrorMessage.NO_WISH;
import static com.sopterm.makeawish.common.message.SuccessMessage.*;
import static java.util.Objects.nonNull;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Tag(name = "Wish", description = "유저 소원 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wishes")
@SecurityRequirement(name = "Authorization")
public class WishController {

	private final WishService wishService;
	private final UserService userService;

	@Operation(summary = "소원 링크 생성")
	@PostMapping
	public ResponseEntity<ApiResponse> createWish(
		@Parameter(hidden = true) @AuthenticationPrincipal InternalMemberDetails memberDetails,
		@RequestBody WishRequestDTO requestDTO
	) {
		userService.checkAbuseUser(memberDetails.getId());
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
	public ResponseEntity<ApiResponse> deleteWishes(
		@Parameter(hidden = true) @AuthenticationPrincipal InternalMemberDetails memberDetails,
		@RequestBody WishIdRequestDTO requestDTO
	) {
		wishService.deleteWishes(memberDetails.getId(), requestDTO);
		return ResponseEntity.ok(success(SUCCESS_DELETE_WISHES.getMessage()));
	}

	@Operation(summary = "진행 중인 소원 수정", description = "수정되지 않은 정보는 null 또는 원래의 정보 그대로 전달")
	@PutMapping("/progress")
	public ResponseEntity<ApiResponse> updateUserMainWish(
		@Parameter(hidden = true) @AuthenticationPrincipal InternalMemberDetails memberDetails,
		@RequestBody UserWishUpdateRequestDTO requestDTO
	) {
		val wish = wishService.updateUserMainWish(memberDetails.getId(), requestDTO);
		return ResponseEntity.ok(ApiResponse.success(SUCCESS_UPDATE_USER_INFO.getMessage(), wish));
	}

	@Operation(summary = "진행 중인 소원 정보 조회")
	@GetMapping("/progress")
	public ResponseEntity<ApiResponse> findUserMainWish(
		@Parameter(hidden = true) @AuthenticationPrincipal InternalMemberDetails memberDetails
	) {
		val response = wishService.findUserMainWish(memberDetails.getId());
		return nonNull(response)
			? ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_USER_INFO.getMessage(), response))
			: ResponseEntity.ok(ApiResponse.fail(EXPIRED_BIRTHDAY_WISH.getMessage()));
	}

	@Operation(summary = "소원 펀딩 중지")
	@PatchMapping("/progress")
	public ResponseEntity<ApiResponse> stopWish(
		@Parameter(hidden = true) @AuthenticationPrincipal InternalMemberDetails memberDetails
	) throws AccessDeniedException {
		wishService.stopWish(memberDetails.getId());
		return ResponseEntity.ok(success(SUCCESS_STOP_WISH.getMessage()));
	}

	private URI getURI(Long id) {
		return ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(id)
			.toUri();
	}
}
