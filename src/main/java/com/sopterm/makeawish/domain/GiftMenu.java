package com.sopterm.makeawish.domain;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@AllArgsConstructor
public class GiftMenu{
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "gift_menu_id")
    private Long id;

    private String name;

    private int price;

    public static GiftMenu getLetter(){
        return new GiftMenu(0L, "letter", 0);
    }
}
