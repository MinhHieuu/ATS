package com.example.ats.application.port.out;

import com.example.ats.domain.model.Resume;

import java.util.List;

public interface ResumeRepository {
    Resume save(Resume resume);
    Resume findById(Long id);
    List<Resume> findAll();
    List<Resume> findByCandidate(Long candidateId);
    void deleteById(Long id);
}
