package com.example.ats.application.port.out;

import com.example.ats.domain.model.Candidate;
import com.example.ats.domain.view.CandidateView;

import java.util.List;

public interface CandidateRepository {
    Candidate save(Candidate candidate);
    Candidate findById(Long id);
    List<CandidateView> findAllWithUser();
}

