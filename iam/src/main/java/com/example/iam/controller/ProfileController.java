package com.example.iam.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ProfileController {

    @GetMapping("/profile")
    public ResponseEntity<?> profile(Authentication authentication) {

        return ResponseEntity.ok(
                Map.of(
                        "email", authentication.getPrincipal(),
                        "roles", authentication.getAuthorities()
                )
        );
    }

}
