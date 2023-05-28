package com.sopterm.makeawish.domain.user;

import static jakarta.persistence.GenerationType.*;

import com.sopterm.makeawish.dto.auth.SignupRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.sopterm.makeawish.domain.wish.AccountInfo;
import com.sopterm.makeawish.domain.wish.Wish;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "member")
public class User implements UserDetails {

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

    private LocalDateTime birthStartAt;

    private LocalDateTime birthEndAt;

    @Embedded
    private AccountInfo account;

    private String phoneNumber;

    @OneToMany(mappedBy = "wisher")
    private final List<Wish> wishes = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Builder
    public User(SignupRequest signupRequest) {
        this.email = signupRequest.getEmail();
        this.socialType = signupRequest.getSocialType();
        this.socialId = signupRequest.getSocialId();
        this.nickname = signupRequest.getNickname();
        this.createdAt = signupRequest.getCreatedAt();
        this.account = new AccountInfo(null, null, null);
    }

    public void updateMemberProfile(
            LocalDateTime birthStartAt,
            LocalDateTime birthEndAt,
            String name,
            String bank,
            String account,
            String phoneNumber) {
        this.birthEndAt = birthEndAt == null ? this.birthEndAt : birthEndAt;
        this.birthStartAt = birthStartAt == null ? this.birthStartAt : birthStartAt;
        this.phoneNumber = phoneNumber == null ? this.phoneNumber : phoneNumber;
        this.account = updateAccount(name,bank,account);
    }

    private AccountInfo updateAccount(String name, String bank, String account) {
        if(name == null) {
            name = this.account.getName();
        }
        if(bank == null) {
            bank = this.account.getBank();
        }
        if(account == null) {
            account = this.account.getAccount();
        }
        return new AccountInfo(name,bank,account);
    }
}