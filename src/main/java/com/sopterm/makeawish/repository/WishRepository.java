package com.sopterm.makeawish.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.domain.wish.Wish;

public interface WishRepository extends JpaRepository<Wish, Long> {

	@Query("select w from Wish w where w.wisher = :wisher and w.startAt <= :now and w.endAt >= :now")
	Optional<Wish> findMainWish(User wisher, LocalDateTime now);

	@Query("select count(w.id) > 0 from Wish w where w.wisher = :wisher and w.startAt <= :now and w.endAt >= :now")
	boolean existsMainWish(User wisher, LocalDateTime now);

	Optional<Wish> findFirstByWisherOrderByEndAtDesc(User wisher);

	boolean existsWishByWisher(User wisher);
}
