package com.example.ats.domain.result;

import com.example.ats.domain.model.Company;
import com.example.ats.domain.model.Recruiter;
import com.example.ats.domain.model.User;

public record RecruiterResult(
        Recruiter recruiter,
        User user,
        Company company
) {
}
