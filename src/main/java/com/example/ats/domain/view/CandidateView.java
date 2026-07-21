package com.example.ats.domain.view;

import com.example.ats.domain.model.Candidate;
import com.example.ats.domain.model.User;

public record CandidateView(
        Candidate candidate,
        User user
) {
}
