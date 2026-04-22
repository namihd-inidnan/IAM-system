package com.example.iam.service;

import com.example.iam.abac.AbacDecision;
import com.example.iam.model.ExamPaper;
import com.example.iam.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class AbacPolicyService {

    public AbacDecision evaluateExamPaperAccess(
            User user,
            ExamPaper paper,
            LocalTime now
    ) {

        if (!"EXAM".equals(user.getDepartment())) {
            return AbacDecision.deny("User department is not EXAM");
        }

        if (!user.getDepartment().equals(paper.getDepartment())) {
            return AbacDecision.deny("User and resource departments do not match");
        }

        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(18, 0);

        if (now.isBefore(start) || now.isAfter(end)) {
            return AbacDecision.deny("Access outside allowed time window");
        }

        return AbacDecision.allow("ABAC policy satisfied");
    }

}
