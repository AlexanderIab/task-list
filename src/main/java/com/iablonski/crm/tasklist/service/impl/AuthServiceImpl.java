package com.iablonski.crm.tasklist.service.impl;

import com.iablonski.crm.tasklist.service.AuthService;
import com.iablonski.crm.tasklist.web.dto.auth.JwtRequest;
import com.iablonski.crm.tasklist.web.dto.auth.JwtResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public JwtResponse login(JwtRequest loginRequest) {
        return null;
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        return null;
    }
}
