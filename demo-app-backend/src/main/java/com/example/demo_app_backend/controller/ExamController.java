package com.example.demo_app_backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exam")
public class ExamController {

    @GetMapping("/data")
    @PreAuthorize("hasRole('EXAMINER')")
    public String examData() {
        return "Exam data from demo app";
    }
}
