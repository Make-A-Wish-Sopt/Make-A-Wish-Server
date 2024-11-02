package com.sopterm.makeawish.repository;

import com.sopterm.makeawish.domain.AlarmTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<AlarmTemplate, Long> {
    AlarmTemplate findByTemplateName(String templateName);
}
