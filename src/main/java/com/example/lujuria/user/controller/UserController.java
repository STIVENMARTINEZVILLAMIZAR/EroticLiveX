package com.example.lujuria.user.controller;

import com.example.lujuria.auth.security.AppUserPrincipal;
import com.example.lujuria.user.dto.UpdateUserProfileRequest;
import com.example.lujuria.user.dto.UserProfileResponse;
import com.example.lujuria.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserProfileResponse me(@AuthenticationPrincipal AppUserPrincipal principal) {
        return userService.getCurrentProfile(principal);
    }

    @PutMapping("/me")
    public UserProfileResponse updateMe(
        @AuthenticationPrincipal AppUserPrincipal principal,
        @Valid @RequestBody UpdateUserProfileRequest request
    ) {
        return userService.updateCurrentProfile(principal, request);
    }
}
