package com.example.ats.domain.view;

import com.example.ats.domain.model.Company;
import com.example.ats.domain.model.Recruiter;
import com.example.ats.domain.model.User;

public record RecruiterView(
        Recruiter recruiter,
        User user,
        Company company
) {
}
