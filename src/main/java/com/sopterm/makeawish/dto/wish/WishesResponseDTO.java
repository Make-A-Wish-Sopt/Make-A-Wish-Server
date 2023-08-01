package com.sopterm.makeawish.dto.wish;

import java.util.List;

import com.sopterm.makeawish.domain.wish.Wish;

import lombok.Builder;

public record WishesResponseDTO(
	List<WishVO> wishes
) {
	public static WishesResponseDTO of(List<Wish> wishes) {
		return new WishesResponseDTO(wishes.stream().map(WishVO::of).toList());
	}
}

@Builder
record WishVO(
	Long wishId,
	String title,
	String startAt,
	String endAt
) {
	public static WishVO of(Wish wish) {
		return WishVO.builder()
			.wishId(wish.getId())
			.title(wish.getTitle())
			.startAt(wish.getStartAt().toString())
			.endAt(wish.getEndAt().toString())
			.build();
	}
}
