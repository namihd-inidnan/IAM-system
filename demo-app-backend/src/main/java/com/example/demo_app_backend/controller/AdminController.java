package com.example.demo_app_backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/metrics")
    @PreAuthorize("hasRole('ADMIN')")
    public String metrics() {
        return "Admin metrics from demo app";
    }
}
