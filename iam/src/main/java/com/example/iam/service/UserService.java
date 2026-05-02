package com.example.iam.service;

import com.example.iam.dto.RegisterRequest;
import com.example.iam.exception.BadRequestException;
import com.example.iam.exception.ForbiddenException;
import com.example.iam.exception.UnauthorizedException;
import com.example.iam.model.Role;
import com.example.iam.model.User;
import com.example.iam.repository.RoleRepository;
import com.example.iam.repository.UserRepository;
import com.example.iam.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuditService auditService;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       AuditService auditService,
                       JwtUtil jwtUtil) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.auditService = auditService;
        this.jwtUtil = jwtUtil;
    }

    // 🔐 LOGIN
    public String login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            auditService.logEvent(email, "LOGIN", "/auth/login", "DENY", "Invalid password");
            throw new UnauthorizedException("Invalid credentials");
        }

        if (!"ACTIVE".equals(user.getStatus())) {
            auditService.logEvent(email, "LOGIN", "/auth/login", "DENY", "User disabled");
            throw new ForbiddenException("User account is disabled");
        }

        auditService.logEvent(email, "LOGIN", "/auth/login", "ALLOW", "Login successful");

        return jwtUtil.generateToken(user); // ✅ FIXED
    }

    // 📝 REGISTER
    public void registerUser(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Email already registered");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("USER role not found"));

        User user = new User(
                request.getName(),
                request.getEmail(),
                hashedPassword,
                "ACTIVE"
        );

        user.setRoles(Set.of(userRole));

        userRepository.save(user);
    }

    // 🔐 DISABLE USER
    public void disableUser(String adminEmail, String targetEmail) {

        User user = userRepository.findByEmail(targetEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus("DISABLED");
        userRepository.save(user);

        auditService.logEvent(adminEmail, "DISABLE_USER", targetEmail, "ALLOW", "User disabled");
    }

    // 🔐 ENABLE USER
    public void enableUser(String adminEmail, String targetEmail) {

        User user = userRepository.findByEmail(targetEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus("ACTIVE");
        userRepository.save(user);

        auditService.logEvent(adminEmail, "ENABLE_USER", targetEmail, "ALLOW", "User enabled");
    }

    // 👨‍💼 ASSIGN ROLE
    public void assignRole(String adminEmail, String targetEmail, String roleName) {

        User user = userRepository.findByEmail(targetEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        if (user.getRoles().stream().anyMatch(r -> r.getName().equals(roleName))) {
            throw new RuntimeException("Role already assigned");
        }

        user.getRoles().add(role);
        userRepository.save(user);

        auditService.logEvent(adminEmail, "ASSIGN_ROLE", targetEmail, "ALLOW", "Role assigned: " + roleName);
    }

    // ❌ REMOVE ROLE (optional but useful)
    public void removeRole(String adminEmail, String targetEmail, String roleName) {

        User user = userRepository.findByEmail(targetEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean removed = user.getRoles().removeIf(r -> r.getName().equals(roleName));

        if (!removed) {
            throw new RuntimeException("Role not assigned");
        }

        userRepository.save(user);

        auditService.logEvent(adminEmail, "REMOVE_ROLE", targetEmail, "ALLOW", "Role removed: " + roleName);
    }
}