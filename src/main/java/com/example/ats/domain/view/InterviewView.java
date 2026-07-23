package com.example.ats.domain.view;

import com.example.ats.domain.model.Interview;

public record InterviewView(
        Interview interview,
        Long candidateUserId,
        String candidateName,
        String jobTitle
) {}
