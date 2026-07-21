package com.example.ats.application.port.out;

import com.example.ats.domain.model.Candidate;
import com.example.ats.domain.view.CandidateView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CandidateRepository {
    Candidate save(Candidate candidate);
    Candidate findById(Long id);
    Page<CandidateView> findAllWithUser(Pageable pageable);
}

