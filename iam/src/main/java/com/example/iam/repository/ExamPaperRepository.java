package com.example.iam.repository;

import com.example.iam.model.ExamPaper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamPaperRepository
        extends JpaRepository<ExamPaper, Long> {
}
