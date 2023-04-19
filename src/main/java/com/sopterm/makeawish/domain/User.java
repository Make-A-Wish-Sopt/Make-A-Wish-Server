package com.sopterm.makeawish.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "social_type", length = 20, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private SocialType socialType;
    @Column
    private String email;
    @Column(unique = true)
    private String socialId;
    @Column(name = "refresh_token")
    private String refreshToken;
    @Column
    private String name;
    @Column
    private String image;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
    @Override
    public String getPassword() {
        return null;
    }
    @Builder
    public User(SocialType socialType, String socialId, LocalDateTime createdAt) {
        this.socialType = socialType;
        this.socialId = socialId;
        this.createdAt = createdAt;
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
}