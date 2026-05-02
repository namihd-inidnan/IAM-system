package com.example.iam.controller;

import com.example.iam.abac.AbacDecision;
import com.example.iam.dto.PolicySimulationRequest;
import com.example.iam.dto.PolicySimulationResponse;
import com.example.iam.model.User;
import com.example.iam.repository.UserRepository;
import com.example.iam.service.AbacPolicyService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/simulate")
public class PolicySimulationController {

    private final UserRepository userRepository;
    private final AbacPolicyService abacPolicyService;

    public PolicySimulationController(
            UserRepository userRepository,
            AbacPolicyService abacPolicyService
    ) {
        this.userRepository = userRepository;
        this.abacPolicyService = abacPolicyService;
    }

    @PostMapping("/access")
    @PreAuthorize("hasRole('ADMIN')")
    public PolicySimulationResponse simulateAccess(
            @RequestBody PolicySimulationRequest request
    ) {

        // 🔹 Fetch user
        User user = userRepository.findByEmail(request.getUserEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔹 RBAC CHECK (basic)
        boolean hasAccess = user.hasRole(request.getRequiredRole());

        if (!hasAccess) {
            return new PolicySimulationResponse(
                    "FAIL",
                    "SKIPPED",
                    "DENY",
                    "User does not have required role"
            );
        }

        // 🔹 ABAC CHECK
        AbacDecision decision = abacPolicyService.evaluateAccess(
                user,
                request.getResourceType(),
                request.getAction(),
                request.getSimulatedTime()
        );

        return new PolicySimulationResponse(
                "PASS",
                decision.isAllowed() ? "PASS" : "FAIL",
                decision.isAllowed() ? "ALLOW" : "DENY",
                decision.getReason()
        );
    }
}