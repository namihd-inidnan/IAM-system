package com.example.demo_app_backend.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
@Entity
public class Marks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String registrationId;
    private String subject;
    private int marks;
    public Long getId() {
        return id;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public String getSubject() {
        return subject;
    }

    public int getMarks() {
        return marks;
    }

    // 🔹 SETTERS

    public void setId(Long id) {
        this.id = id;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    // getters & setters
}
