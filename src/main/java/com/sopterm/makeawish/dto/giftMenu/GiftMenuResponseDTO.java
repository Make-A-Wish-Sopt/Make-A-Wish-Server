package com.sopterm.makeawish.dto.giftMenu;

import com.sopterm.makeawish.domain.GiftMenu;
import lombok.Builder;

import static lombok.AccessLevel.PRIVATE;

@Builder(access = PRIVATE)
public record GiftMenuResponseDTO(Long giftMenuId, String name, int price) {
    public static GiftMenuResponseDTO from(GiftMenu giftMenu) {
        return GiftMenuResponseDTO.builder()
                .giftMenuId(giftMenu.getId())
                .name(giftMenu.getName())
                .price(giftMenu.getPrice())
                .build();
    }
}
