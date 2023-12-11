package com.sopterm.makeawish.dto.user;

import lombok.Builder;

@Builder
public record UserAccountVerifyRequestDTO(
        String name
        , String BankCode
        , String AccountNumber
) {
}
