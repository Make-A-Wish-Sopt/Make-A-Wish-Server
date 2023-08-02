package com.sopterm.makeawish.dto.wish;

import com.sopterm.makeawish.domain.wish.Wish;
import lombok.Builder;

@Builder
public record MypageWishUpdateRequestDTO(String startDate, String endDate, String name, String bankName, String account, String phone) {
    public static MypageWishUpdateRequestDTO from(Wish wish) {
        return MypageWishUpdateRequestDTO.builder()
                .startDate(wish.getStartAt().toString())
                .endDate(wish.getEndAt().toString())
                .name(wish.getWisher().getAccount().getName())
                .bankName(wish.getWisher().getAccount().getBank())
                .account(wish.getWisher().getAccount().getAccount())
                .phone(wish.getPhoneNumber())
                .build();
    }
}
