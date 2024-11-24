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
	private String kakaoPayCode;
	private boolean forPayCode;

	public AccountInfo updateInfo(String name, String bank, String account, String kakaoPayCode) {
		if(nonNull(account) && !this.account.equals(account)) {
			this.forPayCode = false;
		}
		if(nonNull(kakaoPayCode) && !this.kakaoPayCode.equals(kakaoPayCode)) {
			this.forPayCode = true;
		}
		if (nonNull(name)) {
			this.name = name;
		}
		if (nonNull(bank)) {
			this.bank = bank;
		}
		if (nonNull(account)) {
			this.forPayCode = false;
			this.account = account;
		}

		if(nonNull(kakaoPayCode)) {
			this.forPayCode = true;
			this.kakaoPayCode = kakaoPayCode;
		}
		return this;
	}
}
