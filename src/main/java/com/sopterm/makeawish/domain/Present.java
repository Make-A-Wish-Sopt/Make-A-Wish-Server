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

	public static class PresentBuilder {
		private String name;
		private String message;
		private Wish wish;
		private Cake cake;

		private PresentBuilder() {}

		public static PresentBuilder builder() {
			return new PresentBuilder();
		}

		public PresentBuilder name(String name) {
			this.name = name;
			return this;
		}

		public PresentBuilder message(String message) {
			this.message = message;
			return this;
		}

		public PresentBuilder wish(Wish wish) {
			this.wish = wish;
			return this;
		}

		public PresentBuilder cake(Cake cake) {
			this.cake = cake;
			return this;
		}

		private void setWish(Present present, Wish wish) {
			if (nonNull(present.wish)) {
				present.wish.getPresents().remove(present);
			}
			present.wish = wish;
			if (nonNull(wish)) {
				wish.getPresents().add(present);
			}
		}

		public Present build() {
			Present present = new Present();
			present.name = this.name;
			present.message = this.message;
			setWish(present, this.wish);
			present.cake = this.cake;
			return present;
		}
	}

	public static PresentBuilder builder() {
		return new PresentBuilder();
	}
}
