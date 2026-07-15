package com.example.ats.application.port.out;

import com.example.ats.domain.model.Recruiter;
import com.example.ats.domain.view.RecruiterView;

import java.util.List;

public interface RecruiterRepository {
    Recruiter save(Recruiter recruiter);
    Recruiter findById(Long id);
    List<RecruiterView> findAllWithUser();
}
