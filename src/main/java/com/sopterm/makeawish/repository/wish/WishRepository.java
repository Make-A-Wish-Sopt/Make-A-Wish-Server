package com.sopterm.makeawish.repository.wish;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.domain.wish.Wish;

public interface WishRepository extends JpaRepository<Wish, Long>, WishCustomRepository {

	boolean existsWishByWisher(User wisher);
	List<Wish> findByWisherOrderByStartAtDesc(User wisher);
}
