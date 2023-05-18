package com.sopterm.makeawish.dto.present;

import lombok.Builder;
import com.sopterm.makeawish.domain.Present;

@Builder
public record PresentResponseDto(
        String name,
        String message
) {
    public static PresentResponseDto from(Present present){
        return PresentResponseDto.builder()
                .name(present.getName())
                .message(present.getMessage())
                .build();
    }
}
