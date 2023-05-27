package com.sopterm.makeawish.repository;

import com.sopterm.makeawish.domain.user.SocialType;
import com.sopterm.makeawish.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    public User findBySocialTypeAndSocialId(SocialType socialType, String socialId);
    @Query("select u.id from User u")
    public Long findIdBySocialId(String socialId);
    Optional<User> findBySocialId(String socialId);
    public boolean existsBySocialId(String socialId);
    public boolean existsByRefreshToken(String refreshToken);
}
