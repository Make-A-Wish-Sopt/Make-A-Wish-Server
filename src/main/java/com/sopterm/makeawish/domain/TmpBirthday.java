package com.sopterm.makeawish.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "tmp_birthday")
public class TmpBirthday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "birth_date")
    private String birthDate;
    @Column(name = "phone_number")
    private String phoneNumber;
}


