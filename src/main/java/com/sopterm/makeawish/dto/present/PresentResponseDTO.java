package com.sopterm.makeawish.dto.present;

import lombok.Builder;
import com.sopterm.makeawish.domain.Present;

@Builder
public record PresentResponseDTO(
        String name,
        String message
) {
    public static PresentResponseDTO from(Present present){
        return PresentResponseDTO.builder()
                .name(present.getName())
                .message(present.getMessage())
                .build();
    }
}
