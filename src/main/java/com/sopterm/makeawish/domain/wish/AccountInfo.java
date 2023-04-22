package com.sopterm.makeawish.domain.wish;

import jakarta.persistence.Embeddable;

@Embeddable
public class AccountInfo {
	private String name;
	private String bank;
	private String account;
}
