package com.iablonski.crm.tasklist.web.controller;

import com.iablonski.crm.tasklist.domain.user.User;
import com.iablonski.crm.tasklist.service.AuthService;
import com.iablonski.crm.tasklist.service.UserService;
import com.iablonski.crm.tasklist.web.dto.auth.JwtRequest;
import com.iablonski.crm.tasklist.web.dto.auth.JwtResponse;
import com.iablonski.crm.tasklist.web.dto.user.UserDto;
import com.iablonski.crm.tasklist.web.dto.validation.OnCreate;
import com.iablonski.crm.tasklist.web.mappers.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "Auth Controller", description = "Auth API")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    @Operation(summary = "Login")
    public JwtResponse login(@Validated @RequestBody JwtRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    @Operation(summary = "Register")
    public UserDto register(@Validated(OnCreate.class) @RequestBody UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User crearedUser = userService.create(user);
        return userMapper.toDto(crearedUser);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh token")
    public JwtResponse refresh(@RequestBody String refreshToken) {
        return authService.refresh(refreshToken);
    }
}