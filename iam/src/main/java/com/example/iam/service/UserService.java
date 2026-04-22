package com.example.iam.service;

import com.example.iam.exception.BadRequestException;
import com.example.iam.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.example.iam.dto.RegisterRequest;
import com.example.iam.model.User;
import com.example.iam.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.example.iam.exception.ForbiddenException;
import com.example.iam.exception.UnauthorizedException;
import com.example.iam.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.example.iam.model.Role;
import com.example.iam.repository.RoleRepository;
import com.example.iam.service.AuditService;

import java.util.List;



@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AuditService auditService;
    private final JwtUtil jwtUtil;


    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       AuditService auditService,
                       JwtUtil jwtUtil) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.auditService = auditService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public String login(String email, String password) {

        try {
            var user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
            System.out.println("LOGIN ATTEMPT:");
            System.out.println("INPUT PASSWORD: " + password);
            System.out.println("STORED HASH: " + user.getPasswordHash());

            boolean match = passwordEncoder.matches(password, user.getPasswordHash());

            System.out.println("PASSWORD MATCH: " + match);

            if (!match) {
                throw new UnauthorizedException("Invalid credentials");
            }


            if (!"ACTIVE".equals(user.getStatus())) {
                throw new ForbiddenException("User account is disabled");
            }

            // LOGIN SUCCESS → AUDIT
            auditService.logEvent(
                    user.getEmail(),
                    "LOGIN",
                    "/auth/login",
                    "ALLOW",
                    "Login successful"
            );

            List<String> roles = user.getRoles()
                    .stream()
                    .map(Role::getName)
                    .toList();

            return jwtUtil.generateToken(user.getEmail(), roles);

        } catch (UnauthorizedException | ForbiddenException ex) {

            // LOGIN FAILURE → AUDIT
            auditService.logEvent(
                    email != null ? email : "UNKNOWN",
                    "LOGIN",
                    "/auth/login",
                    "DENY",
                    ex.getMessage()
            );

            throw ex;
        }
    }


    public void registerUser(RegisterRequest request) {

        // 1. Check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Email already registered");

        }

        // 2. Hash password
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Default role USER not found"));


        // 3. Create user
        User user = new User(
                request.getName(),
                request.getEmail(),
                hashedPassword,
                "ACTIVE"
        );
        user.getRoles().add(userRole);

        // 4. Save user
        userRepository.save(user);
    }
    public void disableUser(String adminEmail, String targetEmail) {

        var user = userRepository.findByEmail(targetEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus("DISABLED");
        userRepository.save(user);

        auditService.logEvent(
                adminEmail,
                "DISABLE_USER",
                targetEmail,
                "ALLOW",
                "User disabled by admin"
        );
    }

    public void enableUser(String adminEmail, String targetEmail) {

        var user = userRepository.findByEmail(targetEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus("ACTIVE");
        userRepository.save(user);

        auditService.logEvent(
                adminEmail,
                "ENABLE_USER",
                targetEmail,
                "ALLOW",
                "User enabled by admin"
        );
    }
    public void assignRole(String adminEmail, String targetEmail, String roleName) {

        var user = userRepository.findByEmail(targetEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        var role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        boolean alreadyAssigned = user.getRoles().stream()
                .anyMatch(r -> r.getName().equals(roleName));

        if (alreadyAssigned) {
            throw new RuntimeException("Role already assigned");
        }

        user.getRoles().add(role);
        userRepository.save(user);

        auditService.logEvent(
                adminEmail,
                "ASSIGN_ROLE",
                targetEmail,
                "ALLOW",
                "Assigned role: " + roleName
        );
    }

    public void removeRole(String adminEmail, String targetEmail, String roleName) {

        var user = userRepository.findByEmail(targetEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        var role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        boolean removed = user.getRoles()
                .removeIf(r -> r.getName().equals(roleName));

        if (!removed) {
            throw new RuntimeException("Role not assigned to user");
        }

        userRepository.save(user);

        auditService.logEvent(
                adminEmail,
                "REMOVE_ROLE",
                targetEmail,
                "ALLOW",
                "Removed role: " + roleName
        );
    }



}

