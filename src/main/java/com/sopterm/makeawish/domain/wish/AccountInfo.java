package com.sopterm.makeawish.domain.wish;

import jakarta.persistence.Embeddable;

import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AccountInfo {
	private String name;
	private String bank;
	private String account;
}
