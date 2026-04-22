package com.example.iam.abac;

public class AbacDecision {

    private final boolean allowed;
    private final String reason;

    private AbacDecision(boolean allowed, String reason) {
        this.allowed = allowed;
        this.reason = reason;
    }

    public static AbacDecision allow(String reason) {
        return new AbacDecision(true, reason);
    }

    public static AbacDecision deny(String reason) {
        return new AbacDecision(false, reason);
    }

    public boolean isAllowed() {
        return allowed;
    }

    public String getReason() {
        return reason;
    }
}
