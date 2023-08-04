package com.sopterm.makeawish.dto.user;

import com.sopterm.makeawish.domain.user.AccountInfo;
import com.sopterm.makeawish.domain.user.User;
import lombok.Builder;

import java.util.Objects;

@Builder
public record UserAccountResponseDTO(Long id, AccountInfo accountInfo) {
	private static AccountInfo getUserAccount(User user) {
		if(Objects.isNull(user.getAccount())) return null;
		return new AccountInfo(user.getAccount().getName(),user.getAccount().getBank(),user.getAccount().getAccount());
	}
	public static UserAccountResponseDTO from(User user) {
		return UserAccountResponseDTO.builder()
			.id(user.getId())
			.accountInfo(getUserAccount(user))
			.build();
	}
}
