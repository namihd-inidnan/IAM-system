package com.example.demo_app_backend.controller;

import com.example.demo_app_backend.model.*;
import com.example.demo_app_backend.repository.*;
import com.example.demo_app_backend.service.IamService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final StudentRepository studentRepo;
    private final MarksRepository marksRepo;
    private final AttendanceRepository attendanceRepo;
    private final ComplaintRepository complaintRepo;
    private final IamService iamService;

    public AdminController(StudentRepository studentRepo,
                           MarksRepository marksRepo,
                           AttendanceRepository attendanceRepo,
                           ComplaintRepository complaintRepo,
                           IamService iamService) {
        this.studentRepo = studentRepo;
        this.marksRepo = marksRepo;
        this.attendanceRepo = attendanceRepo;
        this.complaintRepo = complaintRepo;
        this.iamService = iamService;
    }

    // 🔹 VIEW ALL STUDENTS
    @GetMapping("/students")
    public List<StudentProfile> getAllStudents() {
        return studentRepo.findAll();
    }

    // 🔹 ADD / UPDATE MARKS
    @PostMapping("/marks")
    public String updateMarks(@RequestBody Marks marks) {
        marksRepo.save(marks);
        return "Marks updated";
    }

    // 🔹 ADD / UPDATE ATTENDANCE
    @PostMapping("/attendance")
    public String updateAttendance(@RequestBody Attendance attendance) {
        attendanceRepo.save(attendance);
        return "Attendance updated";
    }

    // 🔹 VIEW ALL COMPLAINTS
    @GetMapping("/complaints")
    public List<Complaint> getComplaints() {
        return complaintRepo.findAll();
    }

    // 🔹 UPDATE COMPLAINT STATUS
    @PutMapping("/complaint/{id}")
    public String updateComplaintStatus(@PathVariable Long id,
                                        @RequestParam String status) {

        Complaint c = complaintRepo.findById(id).orElseThrow();
        c.setStatus(status);
        complaintRepo.save(c);

        return "Updated";
    }

    // 🔹 ENABLE USER (IAM)
    @PostMapping("/enable-user")
    public String enableUser(@RequestParam String email,
                             @RequestHeader("Authorization") String token) {

        iamService.enableUser(email, token);
        return "User enabled";
    }

    // 🔹 DISABLE USER (IAM)
    @PostMapping("/disable-user")
    public String disableUser(@RequestParam String email,
                              @RequestHeader("Authorization") String token) {

        iamService.disableUser(email, token);
        return "User disabled";
    }

    // 🔹 ASSIGN ROLE (IAM)
    @PostMapping("/assign-role")
    public String assignRole(@RequestParam String email,
                             @RequestParam String role,
                             @RequestHeader("Authorization") String token) {

        iamService.assignRole(email, role, token);
        return "Role assigned";
    }
}