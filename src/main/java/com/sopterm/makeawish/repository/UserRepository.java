package com.sopterm.makeawish.repository;

import com.sopterm.makeawish.domain.SocialType;
import com.sopterm.makeawish.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public User findBySocialTypeAndSocialId(SocialType socialType, String socialId);
    @Query("select u.id from User u")
    public Long findIdBySocialId(String socialId);
    public boolean existsBySocialId(String socialId);
    public boolean existsByRefreshToken(String refreshToken);
}
