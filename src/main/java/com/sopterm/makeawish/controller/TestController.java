package com.sopterm.makeawish.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sopterm.makeawish.common.ApiResponse;

@RestController
public class TestController {

	@GetMapping("/health")
	public ResponseEntity<ApiResponse> test() {
		ApiResponse response = ApiResponse.success("서버 연결");
		return ResponseEntity.ok(response);
	}
}
