package com.sopterm.makeawish.domain;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntity {

	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createdDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

	@LastModifiedDate
	private LocalDateTime lastModifiedDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

	@PrePersist
	void prePersist() {
		createdDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
		lastModifiedDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
	}

	@PreUpdate
	void preUpdate() {
		lastModifiedDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
	}
}
