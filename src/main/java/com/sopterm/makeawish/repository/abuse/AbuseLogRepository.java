package com.sopterm.makeawish.repository.abuse;

import com.sopterm.makeawish.domain.abuse.AbuseLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbuseLogRepository extends JpaRepository<AbuseLog, Long> {
}
