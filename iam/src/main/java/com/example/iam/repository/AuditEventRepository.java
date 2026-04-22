package com.example.iam.repository;

import com.example.iam.model.AuditEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditEventRepository
        extends JpaRepository<AuditEvent, Long> {

    Page<AuditEvent> findByActor(String actor, Pageable pageable);

    Page<AuditEvent> findByDecision(String decision, Pageable pageable);

    Page<AuditEvent> findByAction(String action, Pageable pageable);
}
