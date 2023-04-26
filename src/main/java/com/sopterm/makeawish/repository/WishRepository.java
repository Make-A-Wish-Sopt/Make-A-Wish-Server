package com.sopterm.makeawish.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sopterm.makeawish.domain.wish.Wish;

public interface WishRepository extends JpaRepository<Wish, Long> {
}
