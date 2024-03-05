package com.sopterm.makeawish.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sopterm.makeawish.common.ApiResponse;

@RestController
@Tag(name = "Test", description = "테스트 API")
public class TestController {

	@GetMapping("/health")
	public ResponseEntity<ApiResponse> test() {
		return ResponseEntity.ok(ApiResponse.success("서버 연결"));
	}
}
