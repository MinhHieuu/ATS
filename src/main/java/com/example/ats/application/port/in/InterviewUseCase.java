package com.example.ats.application.port.in;

import com.example.ats.application.dto.request.InterviewRequest;
import com.example.ats.application.dto.request.InterviewResultRequest;
import com.example.ats.application.dto.response.InterviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InterviewUseCase {
    // Recruiter
    InterviewResponse createByJobOwner(InterviewRequest request, Long recruiterId);
    Page<InterviewResponse> findByJobOwner(Long recruiterId, Pageable pageable);
    InterviewResponse findByIdAndJobOwner(Long id, Long recruiterId);
    Page<InterviewResponse> findByApplicationAndJobOwner(Long applicationId, Long recruiterId, Pageable pageable);
    InterviewResponse updateByJobOwner(Long id, InterviewRequest request, Long recruiterId);
    InterviewResponse updateResultByJobOwner(Long id, InterviewResultRequest request, Long recruiterId);
    void deleteByJobOwner(Long id, Long recruiterId);

    // Admin
    InterviewResponse create(InterviewRequest request);
    Page<InterviewResponse> findAll(Pageable pageable);
    InterviewResponse findById(Long id);
    Page<InterviewResponse> findByApplication(Long applicationId, Pageable pageable);
    InterviewResponse update(Long id, InterviewRequest request);
    InterviewResponse updateResult(Long id, InterviewResultRequest request);
    void delete(Long id);

    // Candidate
    Page<InterviewResponse> findMyInterviews(Long candidateId, Pageable pageable);
    InterviewResponse findMyInterviewById(Long id, Long candidateId);
}
