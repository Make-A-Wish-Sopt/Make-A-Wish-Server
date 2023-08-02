package com.sopterm.makeawish.dto.wish;

import java.util.List;

public record WishIdRequestDTO(
	List<Long> wishes
) {
}
