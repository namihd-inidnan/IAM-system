package com.example.iam.dto;

import java.time.LocalTime;

public class PolicySimulationRequest {

    private String userEmail;
    private String resourceType;   // USER / ADMIN
    private String action;         // READ / WRITE
    private String requiredRole;   // USER / ADMIN
    private LocalTime simulatedTime;;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }


    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRequiredRole() {
        return requiredRole;
    }

    public void setRequiredRole(String requiredRole) {
        this.requiredRole = requiredRole;
    }

    public LocalTime getSimulatedTime() {
        return simulatedTime;
    }

    public void setSimulatedTime(LocalTime simulatedTime) {
        this.simulatedTime = simulatedTime;
    }
}



