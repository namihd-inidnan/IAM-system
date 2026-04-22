package com.example.iam.dto;

public class PolicySimulationResponse {

    private String rbacResult;
    private String abacResult;
    private String finalDecision;
    private String reason;

    public PolicySimulationResponse(
            String rbacResult,
            String abacResult,
            String finalDecision,
            String reason
    ) {
        this.rbacResult = rbacResult;
        this.abacResult = abacResult;
        this.finalDecision = finalDecision;
        this.reason = reason;
    }

    public String getRbacResult() {
        return rbacResult;
    }

    public String getAbacResult() {
        return abacResult;
    }

    public String getFinalDecision() {
        return finalDecision;
    }

    public String getReason() {
        return reason;
    }
}
