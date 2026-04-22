package com.example.iam.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_events")
public class AuditEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String actor; // user email or SYSTEM

    @Column(nullable = false)
    private String action; // LOGIN, ACCESS_RESOURCE, ASSIGN_ROLE

    @Column(nullable = false)
    private String resource; // endpoint or object

    @Column(nullable = false)
    private String decision; // ALLOW / DENY

    @Column(nullable = false)
    private String reason; // why allowed/denied

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public AuditEvent() {
    }

    public AuditEvent(
            String actor,
            String action,
            String resource,
            String decision,
            String reason
    ) {
        this.actor = actor;
        this.action = action;
        this.resource = resource;
        this.decision = decision;
        this.reason = reason;
        this.timestamp = LocalDateTime.now();
    }

    // getters only (immutable record)

    public Long getId() {
        return id;
    }

    public String getActor() {
        return actor;
    }

    public String getAction() {
        return action;
    }

    public String getResource() {
        return resource;
    }

    public String getDecision() {
        return decision;
    }

    public String getReason() {
        return reason;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
