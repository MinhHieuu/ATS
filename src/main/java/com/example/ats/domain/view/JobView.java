package com.example.ats.domain.view;

import com.example.ats.domain.model.Company;
import com.example.ats.domain.model.Job;


public record JobView(
     Company company,
        Job job
) {}
