package com.example.iam.security;

import com.example.iam.service.AuditService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final AuditService auditService;

    public CustomAccessDeniedHandler(AuditService auditService) {
        this.auditService = auditService;
    }

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {

        String actor = "UNKNOWN";
        if (request.getUserPrincipal() != null) {
            actor = request.getUserPrincipal().getName();
        }

        auditService.logEvent(
                actor,
                "ACCESS_RESOURCE",
                request.getRequestURI(),
                "DENY",
                "Insufficient role"
        );

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
}
