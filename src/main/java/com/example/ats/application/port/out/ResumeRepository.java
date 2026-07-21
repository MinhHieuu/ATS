package com.example.ats.application.port.out;

import com.example.ats.domain.model.Resume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ResumeRepository {
    Resume save(Resume resume);
    Resume findById(Long id);
    Page<Resume> findAll(Pageable pageable);
    Page<Resume> findByCandidate(Long candidateId, Pageable pageable);
    void deleteById(Long id);
}
