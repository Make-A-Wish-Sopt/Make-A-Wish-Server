package com.sopterm.makeawish.domain.wish;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfo {
	private String name;
	private String bank;
	private String account;
}
