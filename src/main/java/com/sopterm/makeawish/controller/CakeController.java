package com.sopterm.makeawish.controller;

import com.sopterm.makeawish.common.ApiResponse;
import com.sopterm.makeawish.domain.Cake;
import com.sopterm.makeawish.domain.wish.Wish;
import com.sopterm.makeawish.dto.cake.*;
import com.sopterm.makeawish.service.CakeService;
import com.sopterm.makeawish.service.WishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sopterm.makeawish.common.message.SuccessMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cakes")
public class CakeController {

    private final CakeService cakeService;
    private final WishService wishService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllCakes() {
        List<CakeResponseDTO> response = cakeService.getAllCakes();
        return ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_ALL_CAKE.getMessage(), response));
    }

    @PostMapping("/pay/ready")
    public ResponseEntity<ApiResponse> getKakaoPayReady(@RequestBody CakeReadyRequestDto request) {
        CakeReadyResponseDto response = cakeService.getKakaoPayReady(request);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_READY_KAKAOPAY.getMessage(), response));
    }

    @PostMapping("/pay/approve")
    public ResponseEntity<ApiResponse> createCake(@RequestBody CakeApproveRequestDto request) {
        Cake cake = cakeService.findById(request.cake());
        if (cake.getId() != 1) {
            CakeApproveResponseDto response = cakeService.getKakaoPayApprove(request);
        }
        Wish wish = wishService.getWish(request.wishId());
        CakeCreateResponseDto response= cakeService.createPresent(request.name(), cake, wish, request.message());
        return ResponseEntity.ok(ApiResponse.success(SUCCESS_CREATE_CAKE.getMessage(), response));
    }

    @GetMapping("/pay/approve")
    public ResponseEntity<ApiResponse> getPgToken(@RequestParam String pg_token) {
        return ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_PGTOKEN.getMessage(), pg_token));
    }
}
