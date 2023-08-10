package com.sopterm.makeawish.dto.user;

import com.sopterm.makeawish.domain.user.AccountInfo;

public record UserAccountRequestDTO(
	AccountInfo accountInfo,
	String phone
) {
}
