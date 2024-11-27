package com.sopterm.makeawish.dto.user;

import static java.util.Objects.*;

import com.sopterm.makeawish.domain.user.AccountInfo;
import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.domain.user.UserTransferInfo;
import lombok.Builder;

@Builder
public record UserAccountResponseDTO(
	Long id,
	UserTransferInfo userTransferInfo
) {
	public static UserAccountResponseDTO of(User user) {
		return UserAccountResponseDTO.builder()
			.id(user.getId())
			.userTransferInfo(getUserTransferInfo(user))
			.build();
	}

	private static UserTransferInfo getUserTransferInfo(User user) {
		return nonNull(user.getUserTransferInfo()) ? user.getUserTransferInfo() : null;
	}
}
