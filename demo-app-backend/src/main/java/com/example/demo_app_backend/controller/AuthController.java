package com.example.demo_app_backend.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/register")
    public String register(@RequestBody Object request) {
        System.out.println("REGISTER HIT");
        return "User registered (demo)";
    }

    @PostMapping("/login")
    public String login(@RequestBody Object request) {
        System.out.println("LOGIN HIT");
        return "dummy-jwt-token";
    }
}