package com.sopterm.makeawish.dto.user;

import com.sopterm.makeawish.domain.user.UserTransferInfo;

public record UserAccountRequestDTO(
	UserTransferInfo userTransferInfo
) {
}
