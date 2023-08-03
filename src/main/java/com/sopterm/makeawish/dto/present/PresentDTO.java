package com.sopterm.makeawish.dto.present;

import com.sopterm.makeawish.domain.Cake;

import lombok.Builder;

@Builder
public record PresentDTO(
        Long cakeId,
        String name,
        String imageUrl,
        Long count
) {
    public static PresentDTO from(Cake cake, Long count){
        return PresentDTO.builder()
                .cakeId(cake.getId())
                .name(cake.getName())
                .imageUrl(cake.getImageUrl())
                .count(count)
                .build();
    }
}
