package com.sopterm.makeawish.dto.user;

import com.sopterm.makeawish.domain.user.TransferInfo;
import com.sopterm.makeawish.domain.user.User;
import lombok.Builder;

import static java.util.Objects.nonNull;

@Builder
public record UserAccountResponseDTO(
        Long id,
        TransferInfo transferInfo
) {
    public static UserAccountResponseDTO of(User user) {
        return UserAccountResponseDTO.builder()
                .id(user.getId())
                .transferInfo(getUserTransferInfo(user))
                .build();
    }

    private static TransferInfo getUserTransferInfo(User user) {
        return nonNull(user.getTransferInfo()) ? user.getTransferInfo() : null;
    }
}
