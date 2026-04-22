package com.example.iam.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/home")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> userHome() {
        return ResponseEntity.ok("Welcome USER 🙌");
    }
}
