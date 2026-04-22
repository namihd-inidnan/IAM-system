package com.example.iam.service;

import com.example.iam.model.AuditEvent;
import com.example.iam.repository.AuditEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
@Service
public class AuditService {

    private final AuditEventRepository auditEventRepository;

    public AuditService(AuditEventRepository auditEventRepository) {
        this.auditEventRepository = auditEventRepository;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logEvent(
            String actor,
            String action,
            String resource,
            String decision,
            String reason
    ) {

        AuditEvent event = new AuditEvent(
                actor,
                action,
                resource,
                decision,
                reason
        );

        auditEventRepository.save(event);
    }
}
