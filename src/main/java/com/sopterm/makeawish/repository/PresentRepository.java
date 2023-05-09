package com.sopterm.makeawish.repository;

import com.sopterm.makeawish.domain.Present;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PresentRepository extends JpaRepository<Present, Long> {
}
