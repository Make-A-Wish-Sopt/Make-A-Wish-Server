package com.sopterm.makeawish.repository.abuse;

import com.sopterm.makeawish.domain.abuse.AbuseUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AbuseUserRepository extends JpaRepository<AbuseUser, Long> {
    Optional<AbuseUser> findAbuseUserByUserId(Long userId);
}
