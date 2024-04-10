package com.iablonski.crm.tasklist.web.dto.auth;

import jakarta.validation.constraints.NotNull;

public record JwtRequest(
        @NotNull(message = "Username must be not null")
        String username,
        @NotNull(message = "Password must be not null")
        String password) {
}
