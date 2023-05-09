package com.sopterm.makeawish.domain;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static java.util.Objects.*;

import com.sopterm.makeawish.domain.wish.Wish;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Present {

	@Id @GeneratedValue(strategy = IDENTITY)
	@Column(name = "present_id")
	private Long id;

	private String name;

	@Column(columnDefinition = "TEXT")
	private String message;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "wish_id")
	private Wish wish;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "cake_id")
	private Cake cake;

	public Present(String name, String message, Wish wish, Cake cake) {
		this.name = name;
		this.message = message;
		setWish(wish);
		this.cake = cake;
	}

	private void setWish(Wish wish) {
		if (nonNull(this.wish)) {
			this.wish.getPresents().remove(this);
		}
		this.wish = wish;
		wish.getPresents().add(this);
	}
}
