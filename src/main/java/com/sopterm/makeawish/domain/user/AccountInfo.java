package com.sopterm.makeawish.domain.user;

import static java.util.Objects.*;

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

	public AccountInfo updateInfo(String name, String bank, String account) {
		if (nonNull(name)) {
			this.name = name;
		}
		if (nonNull(bank)) {
			this.bank = bank;
		}
		if (nonNull(account)) {
			this.account = account;
		}
		return this;
	}
}
