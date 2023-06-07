package com.sopterm.makeawish.repository;

import com.sopterm.makeawish.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySocialId(String socialId);
    public boolean existsBySocialId(String socialId);
    public boolean existsByRefreshToken(String refreshToken);
}
