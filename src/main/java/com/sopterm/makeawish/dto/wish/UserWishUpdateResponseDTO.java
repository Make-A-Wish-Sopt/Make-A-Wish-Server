package com.sopterm.makeawish.dto.wish;

import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.domain.user.TransferInfo;
import com.sopterm.makeawish.domain.wish.Wish;
import com.sopterm.makeawish.domain.wish.WishStatus;
import lombok.Builder;

import static java.util.Objects.nonNull;

@Builder
public record UserWishUpdateResponseDTO(
        String startDate,
        String endDate,
        TransferInfo transferInfo,
        String imageUrl,
        String title,
        WishStatus status,
        String hint,
        boolean wantsGift
) {
    public static UserWishUpdateResponseDTO of(User user, Wish wish) {
        return UserWishUpdateResponseDTO.builder()
                .startDate(wish.getStartAt().toString())
                .endDate(wish.getEndAt().toString())
                .transferInfo(nonNull(user.getTransferInfo()) ? user.getTransferInfo() : null)
                .imageUrl(wish.getPresentImageUrl())
                .title(wish.getTitle())
                .status(wish.getStatus(0))
                .hint(wish.getHint())
                .wantsGift(wish.isWantsGift())
                .build();
    }
}
