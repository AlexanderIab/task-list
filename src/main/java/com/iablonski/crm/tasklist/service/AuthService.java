package com.iablonski.crm.tasklist.service;

import com.iablonski.crm.tasklist.web.dto.auth.JwtRequest;
import com.iablonski.crm.tasklist.web.dto.auth.JwtResponse;

public interface AuthService {
    JwtResponse login(JwtRequest loginRequest);
    JwtResponse refresh(String refreshToken);

}
