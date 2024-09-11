package com.sopterm.makeawish.repository;

import com.sopterm.makeawish.domain.GiftMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GiftMenuRepository extends JpaRepository<GiftMenu, Long> {
    List<GiftMenu> findAllByOrderById();
}
