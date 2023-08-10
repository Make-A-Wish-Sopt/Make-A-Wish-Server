package com.sopterm.makeawish.dto.user;

import static java.util.Objects.*;

import com.sopterm.makeawish.domain.user.AccountInfo;
import com.sopterm.makeawish.domain.user.User;
import lombok.Builder;

@Builder
public record UserAccountResponseDTO(
	Long id,
	AccountInfo accountInfo,
	String phone
) {
	public static UserAccountResponseDTO of(User user) {
		return UserAccountResponseDTO.builder()
			.id(user.getId())
			.accountInfo(getUserAccount(user))
			.phone(user.getPhoneNumber())
			.build();
	}

	private static AccountInfo getUserAccount(User user) {
		return nonNull(user.getAccount()) ? user.getAccount() : null;
	}
}
