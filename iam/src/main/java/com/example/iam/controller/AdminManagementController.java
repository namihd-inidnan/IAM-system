package com.example.iam.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.iam.service.UserService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/admin/manage")
@PreAuthorize("hasRole('ADMIN')")
public class AdminManagementController {

    private final UserService userService;

    public AdminManagementController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/users/{email}/disable")
    public ResponseEntity<?> disableUser(
            @PathVariable String email,
            Authentication authentication
    ) {
        String adminEmail = authentication.getName();
        userService.disableUser(adminEmail, email);
        return ResponseEntity.ok("User disabled successfully");
    }

    @PostMapping("/users/{email}/enable")
    public ResponseEntity<?> enableUser(
            @PathVariable String email,
            Authentication authentication
    ) {
        String adminEmail = authentication.getName();
        userService.enableUser(adminEmail, email);
        return ResponseEntity.ok("User enabled successfully");
    }
    @PostMapping("/users/{email}/roles")
    public ResponseEntity<?> assignRole(
            @PathVariable String email,
            @RequestParam String role,
            Authentication authentication
    ) {
        String adminEmail = authentication.getName();
        userService.assignRole(adminEmail, email, role);
        return ResponseEntity.ok("Role assigned successfully");
    }

    @PostMapping("/users/{email}/roles/remove")
    public ResponseEntity<?> removeRole(
            @PathVariable String email,
            @RequestParam String role,
            Authentication authentication
    ) {
        String adminEmail = authentication.getName();
        userService.removeRole(adminEmail, email, role);
        return ResponseEntity.ok("Role removed successfully");
    }

}
