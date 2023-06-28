package com.sopterm.makeawish.domain.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class InternalMemberDetails implements UserDetails {
    private Collection<? extends GrantedAuthority> authorities;
    private final Long id;
    private final String username;
    private final String authUserId;

    public InternalMemberDetails(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.authUserId = user.getSocialId();
        this.authorities = List.of(new SimpleGrantedAuthority("MEMBER"));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() { return id; }

    @Override
    public String getPassword() {
        return authUserId;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
