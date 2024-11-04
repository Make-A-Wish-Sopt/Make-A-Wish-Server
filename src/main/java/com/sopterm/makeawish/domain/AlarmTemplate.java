package com.sopterm.makeawish.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
public class AlarmTemplate {
    @Id
    @Column(name = "template_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "template_content")
    private String content;

    @Column(name = "template_button_url")
    private String buttonUrl;

    @Column(name = "template_variables")
    private String variables;

    @Column(name = "template_code")
    private String code;

    @Column(name = "template_image_url")
    private String imageUrl;

    @Column(name = "template_button")
    private String button;

    @Column(name = "template_name")
    private String name;
}
