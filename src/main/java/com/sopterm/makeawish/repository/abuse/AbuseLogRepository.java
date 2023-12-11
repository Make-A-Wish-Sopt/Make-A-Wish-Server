package com.sopterm.makeawish.repository.abuse;

import com.sopterm.makeawish.domain.abuse.AbuseLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AbuseLogRepository extends JpaRepository<AbuseLog, Long> {
    @Query(value = "SELECT COUNT(AL) FROM AbuseLog AL WHERE AL.user = :userId and AL.createdAt >= (now() - interval '7 days')", nativeQuery = true)
    Integer countAbuseLogByUserIdDuringWeekend(@Param("userId") Long userId);
}
