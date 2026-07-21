package com.example.ats.domain.view;

import com.example.ats.domain.model.Candidate;
import com.example.ats.domain.model.Company;
import com.example.ats.domain.model.Job;
import com.example.ats.domain.model.JobApplication;
import com.example.ats.domain.model.Resume;
import com.example.ats.domain.model.User;

public record JobApplicationView(
        JobApplication application,
        Candidate candidate,
        User candidateUser,
        Job job,
        Company jobCompany,
        User jobCreatedBy,
        Resume resume
) {}
