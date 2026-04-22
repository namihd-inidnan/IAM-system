package com.example.iam.controller;

import com.example.iam.model.ExamPaper;
import com.example.iam.model.User;
import com.example.iam.repository.ExamPaperRepository;
import com.example.iam.repository.UserRepository;
import com.example.iam.service.AbacPolicyService;
import com.example.iam.service.AuditService;
import org.springframework.security.core.Authentication;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.iam.abac.AbacDecision;

import java.time.LocalTime;

@RestController
@RequestMapping("/exam")
public class ExamController {

    private final AbacPolicyService abacPolicyService;
    private final UserRepository userRepository;
    private final ExamPaperRepository examPaperRepository;
    private final AuditService auditService;


    public ExamController(
            AbacPolicyService abacPolicyService,
            UserRepository userRepository,
            ExamPaperRepository examPaperRepository,
            AuditService auditService
    ) {
        this.abacPolicyService = abacPolicyService;
        this.userRepository = userRepository;
        this.examPaperRepository = examPaperRepository;
        this.auditService = auditService;
    }


    @GetMapping("/papers")
    @PreAuthorize("hasRole('EXAMINER')")
    public ResponseEntity<?> getExamPapers(Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ExamPaper paper = examPaperRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Paper not found"));

        AbacDecision decision = abacPolicyService.evaluateExamPaperAccess(
                user,
                paper,
                LocalTime.now()
        );

        auditService.logEvent(
                user.getEmail(),
                "ACCESS_EXAM_PAPER",
                "exam_papers/" + paper.getId(),
                decision.isAllowed() ? "ALLOW" : "DENY",
                decision.getReason()
        );

        if (!decision.isAllowed()) {
            throw new AccessDeniedException("ABAC denied: " + decision.getReason());
        }

        return ResponseEntity.ok("Exam papers content");
    }
}
