package com.example.demo_app_backend.repository;

import com.example.demo_app_backend.model.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findByRegistrationId(String registrationId);
}
