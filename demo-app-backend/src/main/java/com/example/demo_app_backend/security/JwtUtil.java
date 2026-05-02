package com.example.demo_app_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.List;

@Component
public class JwtUtil {

    // 🔥 MUST MATCH IAM SECRET EXACTLY
    private static final String SECRET =
            "mysecretkeymysecretkeymysecretkey123"; // same as IAM

    private static final Key key =
            Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    // 🔍 VALIDATE TOKEN
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 🔍 EXTRACT EMAIL
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    // 🔍 EXTRACT ROLES
    public List<String> extractRoles(String token) {
        return extractClaims(token).get("roles", List.class);
    }

    // 🔍 COMMON CLAIM EXTRACTOR
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}