package com.sopterm.makeawish.dto.wish;

import com.sopterm.makeawish.domain.wish.Wish;
import lombok.Builder;

@Builder
public record MypageWishUpdateRequestDTO(String birthStartAt, String birthEndAt, String name, String bankName, String account, String phone) {
    public static MypageWishUpdateRequestDTO from(Wish wish) {
        return MypageWishUpdateRequestDTO.builder()
                .birthStartAt(wish.getStartAt().toString())
                .birthEndAt(wish.getEndAt().toString())
                .name(wish.getWisher().getAccount().getName())
                .bankName(wish.getWisher().getAccount().getBank())
                .account(wish.getWisher().getAccount().getAccount())
                .phone(wish.getPhoneNumber())
                .build();
    }
}
