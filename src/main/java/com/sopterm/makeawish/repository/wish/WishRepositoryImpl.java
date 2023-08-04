package com.sopterm.makeawish.repository.wish;

import static com.sopterm.makeawish.domain.wish.QWish.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.domain.wish.Wish;

import lombok.*;

@Repository
@RequiredArgsConstructor
public class WishRepositoryImpl implements WishCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Optional<Wish> findMainWish(User wisher, int expiryDay) {
		val now = getNowDate(LocalDateTime.now());
		return Optional.ofNullable(queryFactory
			.select(wish)
			.from(wish)
			.where(
				wish.wisher.eq(wisher),
				wish.startAt.before(now),
				wish.endAt.goe(now.minusDays(expiryDay))
			)
			.fetchFirst()
		);
	}

	private LocalDateTime getNowDate(LocalDateTime now) {
		return LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0);
	}

	@Override
	public boolean existsConflictWish(User wisher, LocalDateTime startAt, LocalDateTime endAt, int expiryDay) {
		return queryFactory
			.select(wish)
			.from(wish)
			.where(
				wish.wisher.eq(wisher),
				conflictTerm(startAt, endAt, expiryDay)
			)
			.fetchFirst() != null;
	}

	@Override
	public Optional<Wish> findWishIsUpdatable(User wisher) {
		val now = getNowDate(LocalDateTime.now());
		return Optional.ofNullable(queryFactory
				.selectFrom(wish)
				.where(
						wish.wisher.eq(wisher),
						wish.startAt.before(now).and(wish.endAt.after(now))
				)
				.fetchFirst());
	}

	private BooleanBuilder conflictTerm(LocalDateTime from, LocalDateTime to, int expiryDay) {
		val booleanBuilder = new BooleanBuilder();
		booleanBuilder.or(wish.startAt.between(from, to));
		booleanBuilder.or(wish.endAt.between(from, to));
		booleanBuilder.or(wish.endAt.after(from.minusDays(expiryDay)));
		booleanBuilder.or(wish.endAt.eq(from.minusDays(expiryDay)));
		return booleanBuilder;
	}
}
