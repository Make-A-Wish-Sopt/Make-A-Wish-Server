package com.sopterm.makeawish.controller;

import com.sopterm.makeawish.common.ApiResponse;
import com.sopterm.makeawish.dto.present.PresentDto;
import com.sopterm.makeawish.dto.present.PresentResponseDto;
import com.sopterm.makeawish.service.CakeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

import static com.sopterm.makeawish.common.message.ErrorMessage.NULL_PRINCIPAL;
import static com.sopterm.makeawish.common.message.SuccessMessage.SUCCESS_GET_ALL_PRESENT;
import static com.sopterm.makeawish.common.message.SuccessMessage.SUCCESS_GET_PRESENT_MESSAGE;
import static java.util.Objects.isNull;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cakes")
public class CakeController {

    private final CakeService cakeService;

    @Operation(summary = "해당 소원에 대한 케이크 결과 조회")
    @GetMapping("/{wishId}")
    public ResponseEntity<ApiResponse> getPresents(Principal principal, @PathVariable("wishId") Long wishId) {
        Long userId = getUserId(principal);
        List<PresentDto> response = cakeService.getPresents(userId, wishId);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_ALL_PRESENT.getMessage(), response));
    }

    @Operation(summary = "해당 소원에 대한 케이크 조회")
    @GetMapping("/{wishId}/{cakeId}")
    public ResponseEntity<ApiResponse> getEachPresent(Principal principal, @PathVariable("wishId") Long wishId, @PathVariable("cakeId") Long cakeId) {
        Long userId = getUserId(principal);
        List<PresentResponseDto> response = cakeService.getEachPresent(userId, wishId, cakeId);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_PRESENT_MESSAGE.getMessage(), response));
    }


    private Long getUserId(Principal principal) {
        if (isNull(principal)) {
            throw new IllegalArgumentException(NULL_PRINCIPAL.getMessage());
        }
        return Long.valueOf(principal.getName());
    }
}
