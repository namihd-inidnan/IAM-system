package com.example.demo_app_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class StudentProfile {

    @Id
    private String registrationId;

    private String email;
    private String name;
    private String course;
    private String section;
    private int semester;

    // 🔹 GETTERS

    public String getRegistrationId() {
        return registrationId;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getCourse() {
        return course;
    }

    public String getSection() {
        return section;
    }

    public int getSemester() {
        return semester;
    }

    // 🔹 SETTERS

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }
}