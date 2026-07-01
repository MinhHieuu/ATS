package com.example.ats.application.port.out;

import com.example.ats.domain.model.Recruiter;
import com.example.ats.domain.result.RecruiterResult;

import java.util.List;

public interface RecruiterRepository {
    Recruiter save(Recruiter recruiter);
    Recruiter findById(Long id);
    Recruiter findByUserId(Long userId);
    List<RecruiterResult> findAllWithUser();
}
