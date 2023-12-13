package com.sopterm.makeawish.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record UserAccountVerifyRequestDTO(
        String name,
        @JsonProperty("bankCode")
        String BankCode,
        @JsonProperty("accountNumber")
        String AccountNumber
) {
}
