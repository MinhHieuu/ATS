package com.example.ats.domain.view;

import com.example.ats.domain.model.Company;
import com.example.ats.domain.model.Job;
import com.example.ats.domain.model.User;


public record JobView(
     Company company,
        Job job,
        User createdBy
) {}
