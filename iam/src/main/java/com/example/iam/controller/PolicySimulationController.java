package com.example.iam.controller;

import com.example.iam.abac.AbacDecision;
import com.example.iam.dto.PolicySimulationRequest;
import com.example.iam.dto.PolicySimulationResponse;
import com.example.iam.model.ExamPaper;
import com.example.iam.model.User;
import com.example.iam.repository.ExamPaperRepository;
import com.example.iam.repository.UserRepository;
import com.example.iam.service.AbacPolicyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/simulate")
public class PolicySimulationController {

    private final UserRepository userRepository;
    private final ExamPaperRepository examPaperRepository;
    private final AbacPolicyService abacPolicyService;

    public PolicySimulationController(
            UserRepository userRepository,
            ExamPaperRepository examPaperRepository,
            AbacPolicyService abacPolicyService
    ) {
        this.userRepository = userRepository;
        this.examPaperRepository = examPaperRepository;
        this.abacPolicyService = abacPolicyService;
    }

    @PostMapping("/access")
    @PreAuthorize("hasRole('ADMIN')")
    public PolicySimulationResponse simulateAccess(
            @RequestBody PolicySimulationRequest request
    ) {

        User user = userRepository.findByEmail(request.getUserEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ExamPaper paper = examPaperRepository.findById(request.getExamPaperId())
                .orElseThrow(() -> new RuntimeException("Exam paper not found"));

        // RBAC check (simple)
        boolean hasRole = user.getRoles().stream()
                .anyMatch(r -> r.getName().equals("EXAMINER"));

        if (!hasRole) {
            return new PolicySimulationResponse(
                    "FAIL",
                    "SKIPPED",
                    "DENY",
                    "User does not have EXAMINER role"
            );
        }

        // ABAC check
        AbacDecision decision = abacPolicyService.evaluateExamPaperAccess(
                user,
                paper,
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
