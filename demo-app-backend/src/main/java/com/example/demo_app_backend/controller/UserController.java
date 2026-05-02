package com.example.demo_app_backend.controller;

import com.example.demo_app_backend.model.*;
import com.example.demo_app_backend.repository.*;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final StudentRepository studentRepo;
    private final MarksRepository marksRepo;
    private final AttendanceRepository attendanceRepo;
    private final ComplaintRepository complaintRepo;

    public UserController(StudentRepository studentRepo,
                          MarksRepository marksRepo,
                          AttendanceRepository attendanceRepo,
                          ComplaintRepository complaintRepo) {
        this.studentRepo = studentRepo;
        this.marksRepo = marksRepo;
        this.attendanceRepo = attendanceRepo;
        this.complaintRepo = complaintRepo;
    }

    // 🔹 GET PROFILE
    @GetMapping("/profile")
    public StudentProfile getProfile(Authentication auth) {
        String email = auth.getName();
        return studentRepo.findByEmail(email).orElseThrow();
    }

    // 🔹 GET MARKS
    @GetMapping("/marks")
    public List<Marks> getMarks(Authentication auth) {
        String email = auth.getName();
        String regId = studentRepo.findByEmail(email).orElseThrow().getRegistrationId();
        return marksRepo.findByRegistrationId(regId);
    }

    // 🔹 GET ATTENDANCE
    @GetMapping("/attendance")
    public List<Attendance> getAttendance(Authentication auth) {
        String email = auth.getName();
        String regId = studentRepo.findByEmail(email).orElseThrow().getRegistrationId();
        return attendanceRepo.findByRegistrationId(regId);
    }

    // 🔹 SUBMIT COMPLAINT
    @PostMapping("/complaint")
    public String submitComplaint(@RequestBody Complaint complaint,
                                  Authentication auth) {

        String email = auth.getName();
        String regId = studentRepo.findByEmail(email).orElseThrow().getRegistrationId();

        complaint.setRegistrationId(regId);
        complaint.setStatus("OPEN");

        complaintRepo.save(complaint);

        return "Complaint submitted";
    }
}