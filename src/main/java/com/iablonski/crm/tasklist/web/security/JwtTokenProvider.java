package com.iablonski.crm.tasklist.web.security;

import com.iablonski.crm.tasklist.domain.exception.AccessDeniedException;
import com.iablonski.crm.tasklist.domain.user.Role;
import com.iablonski.crm.tasklist.domain.user.User;
import com.iablonski.crm.tasklist.service.UserService;
import com.iablonski.crm.tasklist.service.props.JwtProperties;
import com.iablonski.crm.tasklist.web.dto.auth.JwtResponse;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private Key key;

    @PostConstruct
    public void init(){
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public String createAccessToken(Long userId, String username, Set<Role> roles){
        Claims claims = Jwts.claims().subject(username).build();
        claims.put("id", userId);
        claims.put("roles", resolveRoles(roles));
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getAccess());
        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    private List<String> resolveRoles(Set<Role> roles) {
        return roles.stream()
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public String createRefreshToken(Long userId, String username){
        Claims claims = Jwts.claims().subject(username).build();
        claims.put("id", userId);
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getRefresh());
        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public JwtResponse refreshUserToken(String refreshToken){
        if(!validateToken(refreshToken)){
            throw new AccessDeniedException();
        }
        Long userId = Long.valueOf(getId(refreshToken));
        User user = userService.getById(userId);
        JwtResponse jwtResponse = new JwtResponse(
                userId,
                user.getUsername(),
                createAccessToken(userId, user.getUsername(), user.getRoles()),
                createRefreshToken(userId, user.getUsername()));
    }

}
