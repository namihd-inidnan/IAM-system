package com.example.demo_app_backend.repository;

import com.example.demo_app_backend.model.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface StudentRepository extends JpaRepository<StudentProfile, String> {
    Optional<StudentProfile> findByEmail(String email);
}
