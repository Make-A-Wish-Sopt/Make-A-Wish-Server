package com.sopterm.makeawish.controller;

import com.sopterm.makeawish.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.sopterm.makeawish.common.message.SuccessMessage.SUCCESS_GET_PGTOKEN;

@RestController
public class PayController {

    @Operation(summary = "카카오페이 결제 준비 후 pg 토큰 발급")
    @GetMapping("/cakes/approve")
    public ResponseEntity<ApiResponse> getPgToken(@RequestParam("pg_token") String pgToken) {
        return ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_PGTOKEN.getMessage(), pgToken));
    }
}
