package com.sopterm.makeawish.controller;

import com.sopterm.makeawish.common.ApiResponse;
import com.sopterm.makeawish.domain.user.InternalMemberDetails;
import com.sopterm.makeawish.dto.present.PresentDto;
import com.sopterm.makeawish.dto.present.PresentResponseDto;
import com.sopterm.makeawish.service.CakeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.sopterm.makeawish.common.message.SuccessMessage.SUCCESS_GET_ALL_PRESENT;
import static com.sopterm.makeawish.common.message.SuccessMessage.SUCCESS_GET_PRESENT_MESSAGE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cakes")
@SecurityRequirement(name = "Authorization")
public class CakeController {

    private final CakeService cakeService;

    @Operation(summary = "해당 소원에 대한 케이크 결과 조회")
    @GetMapping("/{wishId}")
    public ResponseEntity<ApiResponse> getPresents(@Parameter(hidden = true) @AuthenticationPrincipal InternalMemberDetails memberDetails,
                                                   @PathVariable("wishId") Long wishId) {
        List<PresentDto> response = cakeService.getPresents(memberDetails.getId(), wishId);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_ALL_PRESENT.getMessage(), response));
    }

    @Operation(summary = "해당 소원에 대한 케이크 조회")
    @GetMapping("/{wishId}/{cakeId}")
    public ResponseEntity<ApiResponse> getEachPresent(@Parameter(hidden = true) @AuthenticationPrincipal InternalMemberDetails memberDetails,
                                                      @PathVariable("wishId") Long wishId, @PathVariable("cakeId") Long cakeId) {
        List<PresentResponseDto> response = cakeService.getEachPresent(memberDetails.getId(), wishId, cakeId);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS_GET_PRESENT_MESSAGE.getMessage(), response));
    }
}
