package com.sopterm.makeawish.domain.wish;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class AccountInfo {
	private String name;
	private String bank;
	private String account;

	public AccountInfo(String name, String bank, String account) {
		this.name = name;
		this.bank = bank;
		this.account = account;
	}
}