package com.sopterm.makeawish.repository;

import com.sopterm.makeawish.domain.Present;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PresentRepository extends JpaRepository<Present, Long> {
    Optional<Present> findPresentByWishIdAndId(Long wishId, Long presentId);
}
