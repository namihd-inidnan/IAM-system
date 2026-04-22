package com.example.iam.dto;

import java.time.LocalTime;

public class PolicySimulationRequest {

    private String userEmail;
    private Long examPaperId;
    private LocalTime simulatedTime;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Long getExamPaperId() {
        return examPaperId;
    }

    public void setExamPaperId(Long examPaperId) {
        this.examPaperId = examPaperId;
    }

    public LocalTime getSimulatedTime() {
        return simulatedTime;
    }

    public void setSimulatedTime(LocalTime simulatedTime) {
        this.simulatedTime = simulatedTime;
    }
}
