package com.sopterm.makeawish.repository;

import com.sopterm.makeawish.domain.user.SocialType;
import com.sopterm.makeawish.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    public User findBySocialTypeAndSocialId(SocialType socialType, String socialId);
    Optional<User> findBySocialId(String socialId);
    Optional<User> findById(Long id);
}
