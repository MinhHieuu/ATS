package com.example.ats.application.port.out;

import com.example.ats.domain.model.Recruiter;
import com.example.ats.domain.view.RecruiterView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecruiterRepository {
    Recruiter save(Recruiter recruiter);
    Recruiter findById(Long id);
    Page<RecruiterView> findAllWithUser(Pageable pageable);
}
