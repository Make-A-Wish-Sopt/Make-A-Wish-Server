package com.sopterm.makeawish.domain.user;

import com.sopterm.makeawish.domain.wish.Wish;
import com.sopterm.makeawish.dto.auth.AuthSignInRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;
import static java.util.Objects.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "member")
public class User {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(length = 20, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private SocialType socialType;

    @Column
    private String email;

    @Column
    private String nickname;

    @Column(unique = true)
    private String socialId;

    private String refreshToken;

    private String image;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Embedded
    private AccountInfo account;

    private String phoneNumber;

    @OneToMany(mappedBy = "wisher")
    private final List<Wish> wishes = new ArrayList<>();

    @Builder
    public User(AuthSignInRequestDTO authSignInRequestDto) {
        this.email = authSignInRequestDto.email();
        this.socialType = authSignInRequestDto.socialType();
        this.socialId = authSignInRequestDto.socialId();
        this.nickname = authSignInRequestDto.nickname();
        this.createdAt = authSignInRequestDto.createdAt();
        this.account = new AccountInfo(null, null, null);
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateProfile(String name, String bank, String account, String phoneNumber) {
        updatePhoneNumber(phoneNumber);
        updateAccount(name, bank, account);
    }

    public void updatePhoneNumber(String phoneNumber) {
        if (nonNull(phoneNumber)) {
            this.phoneNumber = phoneNumber;
        }
    }

    public void updateAccount(String name, String bank, String account) {
        if (isNull(this.account)) {
            this.account = new AccountInfo(null, null, null);
        }
        this.account.updateInfo(name, bank, account);
    }
}