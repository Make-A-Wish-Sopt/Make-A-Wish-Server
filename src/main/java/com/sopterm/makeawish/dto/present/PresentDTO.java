package com.sopterm.makeawish.dto.present;

import com.sopterm.makeawish.domain.Cake;

import com.sopterm.makeawish.domain.Present;
import lombok.Builder;

@Builder
public record PresentDTO(
        Long presentId,
        Long cakeId,
        String name
) {
    public static PresentDTO from(Present present, Cake cake, String name){
        return PresentDTO.builder()
                .presentId(present.getId())
                .cakeId(cake.getId())
                .name(name)
                .build();
    }
}
