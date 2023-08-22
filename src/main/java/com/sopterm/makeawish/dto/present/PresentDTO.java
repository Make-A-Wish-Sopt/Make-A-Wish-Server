package com.sopterm.makeawish.dto.present;

import com.sopterm.makeawish.domain.Cake;

import lombok.Builder;

@Builder
public record PresentDTO(
        Long cakeId,
        Long count
) {
    public static PresentDTO from(Cake cake, Long count){
        return PresentDTO.builder()
                .cakeId(cake.getId())
                .count(count)
                .build();
    }
}
