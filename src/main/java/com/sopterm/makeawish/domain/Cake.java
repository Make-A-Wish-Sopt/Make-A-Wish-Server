package com.sopterm.makeawish.domain;

import static jakarta.persistence.GenerationType.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Cake {

	@Id @GeneratedValue(strategy = IDENTITY)
	@Column(name = "cake_id")
	private Long id;

	private String name;

	private int price;

	private String imageUrl;
}
