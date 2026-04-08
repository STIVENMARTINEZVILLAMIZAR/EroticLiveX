package com.example.lujuria.auth.security;

import com.example.lujuria.user.entity.AppUser;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AppUserPrincipal implements UserDetails {

    private final Long userId;
    private final String email;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean active;

    public AppUserPrincipal(
        Long userId,
        String email,
        String password,
        Collection<? extends GrantedAuthority> authorities,
        boolean active
    ) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.active = active;
    }

    public static AppUserPrincipal fromUser(AppUser user) {
        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
            .toList();
        return new AppUserPrincipal(user.getId(), user.getEmail(), user.getPasswordHash(), authorities, user.isActive());
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
