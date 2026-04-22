package com.example.iam.controller;

import com.example.iam.model.AuditEvent;
import com.example.iam.repository.AuditEventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/audit")
public class AdminAuditController {

    private final AuditEventRepository auditEventRepository;

    public AdminAuditController(AuditEventRepository auditEventRepository) {
        this.auditEventRepository = auditEventRepository;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<AuditEvent> getAuditLogs(
            @RequestParam(required = false) String actor,
            @RequestParam(required = false) String decision,
            @RequestParam(required = false) String action,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {

        PageRequest pageable = PageRequest.of(page, size);

        if (actor != null) {
            return auditEventRepository.findByActor(actor, pageable);
        }

        if (decision != null) {
            return auditEventRepository.findByDecision(decision, pageable);
        }

        if (action != null) {
            return auditEventRepository.findByAction(action, pageable);
        }

        return auditEventRepository.findAll(pageable);
    }
}
