package com.sopterm.makeawish.dto.user;

import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.domain.wish.Wish;
import lombok.Builder;

@Builder
public record UserWishUpdateRequestDTO(
    String startDate,
    String endDate,
    String name,
    String bankName,
    String account,
    String phone
) {
    public static UserWishUpdateRequestDTO from(User wisher, Wish wish) {
        return UserWishUpdateRequestDTO.builder()
                .startDate(wish.getStartAt().toString())
                .endDate(wish.getEndAt().toString())
                .name(wisher.getAccount().getName())
                .bankName(wisher.getAccount().getBank())
                .account(wisher.getAccount().getAccount())
                .build();
    }
}
