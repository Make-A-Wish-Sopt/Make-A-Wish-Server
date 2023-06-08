package com.sopterm.makeawish.domain.user;

import static jakarta.persistence.GenerationType.*;
import static java.util.Objects.isNull;

import com.sopterm.makeawish.dto.auth.AuthSignInRequestDto;
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

    private String comment;

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
    public User(AuthSignInRequestDto authSignInRequestDto) {
        this.email = authSignInRequestDto.email();
        this.socialType = authSignInRequestDto.socialType();
        this.socialId = authSignInRequestDto.socialId();
        this.nickname = authSignInRequestDto.nickname();
        this.createdAt = authSignInRequestDto.createdAt();
        this.account = new AccountInfo(null, null, null);
    }

    public void updateMemberProfile(
            LocalDateTime birthStartAt,
            LocalDateTime birthEndAt,
            String name,
            String bank,
            String account,
            String phoneNumber) {
        this.birthEndAt = isNull(birthEndAt) ? this.birthEndAt : birthEndAt;
        this.birthStartAt = isNull(birthStartAt) ? this.birthStartAt : birthStartAt;
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