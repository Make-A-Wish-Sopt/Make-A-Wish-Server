package com.sopterm.makeawish.dto.wish;

import com.sopterm.makeawish.domain.wish.Wish;

import lombok.Builder;

@Builder
public record UserWishResponseDTO(
	String title,
	String startAt,
	String endAt
) {

	public static UserWishResponseDTO of(Wish wish) {
		return UserWishResponseDTO.builder()
			.title(wish.getTitle())
			.startAt(wish.getStartAt().toString())
			.endAt(wish.getEndAt().toString())
			.build();
	}
}
