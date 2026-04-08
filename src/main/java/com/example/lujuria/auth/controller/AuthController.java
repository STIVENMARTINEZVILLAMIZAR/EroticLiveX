package com.example.lujuria.auth.controller;

import com.example.lujuria.auth.dto.AuthResponse;
import com.example.lujuria.auth.dto.LoginRequest;
import com.example.lujuria.auth.dto.RegisterRequest;
import com.example.lujuria.auth.security.AppUserPrincipal;
import com.example.lujuria.auth.service.AuthService;
import com.example.lujuria.user.dto.UserProfileResponse;
import com.example.lujuria.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public UserProfileResponse me(@AuthenticationPrincipal AppUserPrincipal principal) {
        return userService.getCurrentProfile(principal);
    }
}
