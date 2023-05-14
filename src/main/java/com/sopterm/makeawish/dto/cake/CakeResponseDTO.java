package com.sopterm.makeawish.dto.cake;

import static lombok.AccessLevel.*;

import com.sopterm.makeawish.domain.Cake;

import lombok.Builder;

@Builder(access = PRIVATE)
public record CakeResponseDTO(Long cakeId, String name, String imageUrl, int price) {

	public static CakeResponseDTO from(Cake cake) {
		return CakeResponseDTO.builder()
			.cakeId(cake.getId())
			.name(cake.getName())
			.imageUrl(cake.getImageUrl())
			.price(cake.getPrice())
			.build();
	}

	public Cake toEntity(){
		return Cake.builder()
				.id(cakeId)
				.name(name)
				.imageUrl(imageUrl)
				.price(price)
				.build();
	}
}
