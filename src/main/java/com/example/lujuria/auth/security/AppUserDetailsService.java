package com.example.lujuria.auth.security;

import com.example.lujuria.common.ResourceNotFoundException;
import com.example.lujuria.user.entity.AppUser;
import com.example.lujuria.user.repository.AppUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    public AppUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        AppUser user = appUserRepository.findByEmailIgnoreCase(username)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));
        return AppUserPrincipal.fromUser(user);
    }
}
