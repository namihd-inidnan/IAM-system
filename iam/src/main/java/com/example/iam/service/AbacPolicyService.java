package com.example.iam.service;

import com.example.iam.abac.AbacDecision;
import com.example.iam.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class AbacPolicyService {

    public AbacDecision evaluateAccess(
            User user,
            String resourceType,   // e.g., "MARKS", "ATTENDANCE", "ADMIN"
            String action,         // e.g., "READ", "WRITE"
            LocalTime now
    ) {

        // 🔹 ROLE CHECK
        if (resourceType.equals("ADMIN") && !user.hasRole("ADMIN")) {
            return AbacDecision.deny("Only ADMIN can access admin resources");
        }

        // 🔹 USER ACCESS (students)
        if (resourceType.equals("USER") && action.equals("READ")) {
            if (!user.hasRole("USER") && !user.hasRole("ADMIN")) {
                return AbacDecision.deny("Only USER/ADMIN can access user data");
            }
        }

        // 🔹 TIME-BASED ACCESS
        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(20, 0);

        if (now.isBefore(start) || now.isAfter(end)) {
            return AbacDecision.deny("Access outside allowed hours");
        }

        // 🔹 DEFAULT ALLOW
        return AbacDecision.allow("Access granted by ABAC policy");
    }
}