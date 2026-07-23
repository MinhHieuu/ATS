package com.example.ats.domain.view;

import com.example.ats.domain.model.InterviewFeedback;

public record InterviewFeedbackView(
        InterviewFeedback feedback,
        String reviewerName,
        String interviewTitle,
        String candidateName,
        String jobTitle
) {}
