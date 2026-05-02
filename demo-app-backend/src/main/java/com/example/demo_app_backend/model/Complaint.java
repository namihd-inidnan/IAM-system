package com.example.demo_app_backend.model;

import jakarta.persistence.*;

@Entity
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String registrationId;
    private String message;
    private String status;

    // 🔹 GETTERS

    public Long getId() {
        return id;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    // 🔹 SETTERS

    public void setId(Long id) {
        this.id = id;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}