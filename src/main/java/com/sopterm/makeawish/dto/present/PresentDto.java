package com.sopterm.makeawish.dto.present;

import com.sopterm.makeawish.domain.Cake;
import com.sopterm.makeawish.domain.Present;
import lombok.Builder;

@Builder
public record PresentDto(
        Long cakeId,
        String name,
        String imageUrl,
        Long count
) {
    public static PresentDto from(Cake cake, Long count){
        return PresentDto.builder()
                .cakeId(cake.getId())
                .name(cake.getName())
                .imageUrl(cake.getImageUrl())
                .count(count)
                .build();
    }
}
