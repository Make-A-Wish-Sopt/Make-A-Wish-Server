package com.sopterm.makeawish.repository.abuse;

import com.sopterm.makeawish.domain.abuse.AbuseLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AbuseLogRepository extends JpaRepository<AbuseLog, Long> {
    @Query(value = "SELECT COUNT(AL) FROM ABUSE_LOG AL WHERE AL.user_id = :userId and AL.created_at >= (now() - interval '7 days')", nativeQuery = true)
    Integer countAbuseLogByUserIdDuringWeekend(@Param("userId") Long userId);
}
