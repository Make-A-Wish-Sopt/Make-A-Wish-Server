package com.sopterm.makeawish.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sopterm.makeawish.domain.Cake;

public interface CakeRepository extends JpaRepository<Cake, Long> {
}
