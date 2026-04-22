package com.example.iam.controller;

import com.example.iam.dto.LoginRequest;
import com.example.iam.dto.RegisterRequest;
import com.example.iam.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        userService.registerUser(request);

        return ResponseEntity.ok(
                java.util.Map.of("message", "User registered successfully")
        );
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        String token = userService.login(
                request.getEmail(),
                request.getPassword()
        );

        return ResponseEntity.ok(
                java.util.Map.of("access_token", token)
        );
    }

}

