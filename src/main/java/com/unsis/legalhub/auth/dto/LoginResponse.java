package com.unsis.legalhub.auth.dto;

public record LoginResponse(
        String accessToken,
        String tokenType,
        int expiresIn,
        String username,
        String role
) {
}
