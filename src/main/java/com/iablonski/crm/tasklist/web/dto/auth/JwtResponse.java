package com.iablonski.crm.tasklist.web.dto.auth;

public record JwtResponse(Long Id, String username, String accessToken, String refreshToken) {
}
