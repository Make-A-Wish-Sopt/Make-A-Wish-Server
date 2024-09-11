package com.sopterm.makeawish.dto.present;

import lombok.Builder;
import com.sopterm.makeawish.domain.Present;

@Builder
public record PresentResponseDTO(
        String name,
        String message,
        Long cakeId,
        Long giftMenuId
) {
    public static PresentResponseDTO from(Present present){
        return PresentResponseDTO.builder()
                .name(present.getName())
                .cakeId(present.getCake().getId())
                .giftMenuId(present.getGiftMenu().getId())
                .message(present.getMessage())
                .build();
    }
}
