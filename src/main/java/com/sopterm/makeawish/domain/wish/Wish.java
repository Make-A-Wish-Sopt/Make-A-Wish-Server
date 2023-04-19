package com.sopterm.makeawish.domain.wish;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.sopterm.makeawish.domain.Present;
import com.sopterm.makeawish.domain.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;

@Entity
@Getter
public class Wish {

	@Id @GeneratedValue(strategy = IDENTITY)
	@Column(name = "wish_id")
	private Long id;

	private String title;

	private String presentImageUrl;

	@Column(columnDefinition = "TEXT")
	private String hint1;

	private String hint2;

	private LocalDateTime startAt;

	private LocalDateTime endAt;

	@Embedded
	private AccountInfo account;

	private String phoneNumber;

	private int price;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id")
	private User wisher;

	@OneToMany(mappedBy = "wish")
	private List<Present> presents = new ArrayList<>();
}
