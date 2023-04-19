package com.sopterm.makeawish.model.wish;

import jakarta.persistence.Embeddable;

@Embeddable
public class AccountInfo {
	private String name;
	private String bank;
	private String account;
}
