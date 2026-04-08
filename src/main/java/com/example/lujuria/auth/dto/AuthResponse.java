package com.example.lujuria.auth.dto;

import java.util.Set;

public record AuthResponse(
    String token,
    Long userId,
    String email,
    Set<String> roles,
    boolean creator
) {
}
