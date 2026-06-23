package com.example.ats.domain.result;

import com.example.ats.domain.model.Candidate;
import com.example.ats.domain.model.User;

public record CandidateResult(
        Candidate candidate,
        User user
) {
}
