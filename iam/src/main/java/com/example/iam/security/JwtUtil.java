package com.example.iam.security;

import com.example.iam.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    // 🔥 FIXED: STATIC SECRET KEY (DO NOT CHANGE EVERY RUN)
    private static final String SECRET = "mysecretkeymysecretkeymysecretkey123"; // must be long

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    // 🔐 GENERATE TOKEN FROM USER
    public String generateToken(User user) {

        List<String> roles = user.getRoles()
                .stream()
                .map(role -> role.getName())
                .toList();

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 🔍 VALIDATE TOKEN + GET EMAIL
    public String validateTokenAndGetEmail(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    // 🔍 EXTRACT ROLES
    public List<String> extractRoles(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("roles", List.class);
    }
}