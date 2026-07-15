package com.example.ats.domain.view;

import com.example.ats.domain.model.Company;
import com.example.ats.domain.model.Job;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JobView {
    private Company company;
    private Job job;
}
