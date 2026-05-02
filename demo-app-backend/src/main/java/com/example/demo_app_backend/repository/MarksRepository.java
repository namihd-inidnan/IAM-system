package com.example.demo_app_backend.repository;

import com.example.demo_app_backend.model.Marks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarksRepository extends JpaRepository<Marks, Long> {
    List<Marks> findByRegistrationId(String registrationId);
}
