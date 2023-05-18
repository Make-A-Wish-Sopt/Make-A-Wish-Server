package com.sopterm.makeawish.repository;

import com.sopterm.makeawish.domain.Present;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PresentRepository extends JpaRepository<Present, Long> {
    List<Present> findPresentsByWishIdAndCakeId(Long wishId, Long cakeId);
}
