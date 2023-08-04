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
import static java.util.Objects.isNull;

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

    public void updateMemberProfile(
            String name,
            String bank,
            String account,
            String phoneNumber) {
        this.phoneNumber = isNull(phoneNumber) ? this.phoneNumber : phoneNumber;
        this.nickname = isNull(name) ? this.nickname : name;
        this.account = updateAccount(name,bank,account);
    }

    private AccountInfo updateAccount(String name, String bank, String account) {
        if(isNull(name)) {
            name = this.account.getName();
        }
        if(isNull(bank)) {
            bank = this.account.getBank();
        }
        if(isNull(account)) {
            account = this.account.getAccount();
        }
        return new AccountInfo(name,bank,account);
    }
}