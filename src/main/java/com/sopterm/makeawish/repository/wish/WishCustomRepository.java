package com.sopterm.makeawish.repository.wish;

import java.time.LocalDateTime;
import java.util.Optional;

import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.domain.wish.Wish;

public interface WishCustomRepository {

	Optional<Wish> findMainWish(User wisher, int expiryDay);
	boolean existsConflictWish(User wisher, LocalDateTime startAt, LocalDateTime endAt, int expiryDay);
	Optional<Wish> findWishIsNowAvailable(User wisher);
}
