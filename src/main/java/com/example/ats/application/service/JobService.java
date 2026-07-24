package com.example.ats.application.service;

import com.example.ats.application.dto.request.JobRequest;
import com.example.ats.application.dto.response.JobResponse;
import com.example.ats.application.mapper.CategoryMapper;
import com.example.ats.application.mapper.CompanyMapper;
import com.example.ats.application.mapper.JobMapper;
import com.example.ats.application.mapper.UserMapper;
import com.example.ats.application.port.in.AuditLogUseCase;
import com.example.ats.application.port.in.JobUseCase;
import com.example.ats.application.port.in.NotificationUseCase;
import com.example.ats.application.port.out.CategoryRepository;
import com.example.ats.application.port.out.JobRepository;
import com.example.ats.domain.exception.BusinessRuleException;
import com.example.ats.domain.exception.ResourceNotFoundException;
import com.example.ats.domain.model.AuditAction;
import com.example.ats.domain.model.AuditEntityType;
import com.example.ats.domain.model.Category;
import com.example.ats.domain.model.Job;
import com.example.ats.domain.model.JobStatus;
import com.example.ats.domain.model.NotificationType;
import com.example.ats.domain.view.JobView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
public class JobService implements JobUseCase {
    private final JobRepository repository;
    private final CategoryRepository categoryRepository;
    private final JobMapper mapper;
    private final CompanyMapper companyMapper;
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;
    private final NotificationUseCase notificationUseCase;
    private final AuditLogUseCase auditLog;

    public JobService(JobRepository repository, CategoryRepository categoryRepository, JobMapper mapper,
                      CompanyMapper companyMapper, CategoryMapper categoryMapper, UserMapper userMapper,
                      NotificationUseCase notificationUseCase, AuditLogUseCase auditLog) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
        this.companyMapper = companyMapper;
        this.categoryMapper = categoryMapper;
        this.userMapper = userMapper;
        this.notificationUseCase = notificationUseCase;
        this.auditLog = auditLog;
    }

    @Override
    public JobResponse createByRecruiter(JobRequest request) {
        validateCategoryActive(request.getCategoryId());
        return create(request);
    }

    @Override
    public JobResponse updateByRecruiter(Long id, JobRequest request) {
        validateCategoryActive(request.getCategoryId());
        return update(id, request);
    }

    @Override
    public JobResponse create(JobRequest request) {
        Instant now = Instant.now();
        JobStatus status = request.getStatus() != null ? request.getStatus() : JobStatus.OPEN;
        Job job = new Job(null, request.getTitle(), request.getDescription(), request.getRequirements(),
                request.getLocation(), request.getEmploymentType(), request.getCompanyId(), request.getCategoryId(),
                request.getSalaryMin(), request.getSalaryMax(), status, request.getCreatedBy(), now, null);
        JobView view = repository.save(job);
        String creatorName = view.createdBy() != null ? view.createdBy().getFullname() : "Unknown";
        notificationUseCase.sendToAdmins(NotificationType.JOB_CREATED,
                "Job mới được tạo",
                creatorName + " đã tạo job: " + view.job().getTitle(),
                view.job().getId(), null);
        auditLog.log(AuditAction.JOB_CREATED, AuditEntityType.JOB, view.job().getId(),
                "Tạo tin tuyển dụng: " + view.job().getTitle());
        return toResponse(view);
    }

    @Override
    public JobResponse update(Long id, JobRequest request) {
        Job job = repository.findById(id).job();
        JobStatus oldStatus = job.getStatus();
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setRequirements(request.getRequirements());
        job.setLocation(request.getLocation());
        job.setEmploymentType(request.getEmploymentType());
        job.setCompanyId(request.getCompanyId());
        job.setCategoryId(request.getCategoryId());
        job.setSalaryMin(request.getSalaryMin());
        job.setSalaryMax(request.getSalaryMax());
        if (request.getStatus() != null) {
            job.setStatus(request.getStatus());
        }
        job.setUpdatedAt(Instant.now());
        JobView view = repository.save(job);
        auditLog.log(AuditAction.JOB_UPDATED, AuditEntityType.JOB, id,
                "Cập nhật tin tuyển dụng: " + view.job().getTitle());
        // Neu chinh sua chung cung doi luon trang thai, ghi them JOB_STATUS_CHANGED de audit trail
        // dong nhat voi luong activate/deactivate.
        JobStatus newStatus = view.job().getStatus();
        if (oldStatus != newStatus) {
            auditLog.log(AuditAction.JOB_STATUS_CHANGED, AuditEntityType.JOB, id,
                    "Đổi trạng thái job #" + id + " từ " + oldStatus + " sang " + newStatus,
                    oldStatus != null ? oldStatus.name() : null, newStatus != null ? newStatus.name() : null);
        }
        return toResponse(view);
    }

    @Override
    public JobResponse activate(Long id) {
        return updateStatus(id, JobStatus.OPEN);
    }

    @Override
    public JobResponse deactivate(Long id) {
        return updateStatus(id, JobStatus.CLOSED);
    }

    @Override
    public JobResponse findById(Long id) {
        return toResponse(repository.findById(id));
    }

    @Override
    public JobResponse findByIdNotClosed(Long id) {
        JobView view = repository.findById(id);
        // Bao 404 thay vi 403 de khong lo ra su ton tai cua job da dong.
        if (view.job().getStatus() == JobStatus.CLOSED) {
            throw new ResourceNotFoundException("Job not found");
        }
        return toResponse(view);
    }

    @Override
    public Page<JobResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    @Override
    public Page<JobResponse> findAllNotClosed(Pageable pageable) {
        return repository.findByStatusNot(JobStatus.CLOSED, pageable).map(this::toResponse);
    }

    @Override
    public Page<JobResponse> findByCreatedBy(Long createdBy, Pageable pageable) {
        return repository.findByCreatedBy(createdBy, pageable).map(this::toResponse);
    }

    @Override
    public Page<JobResponse> searchByTitle(String title, Pageable pageable) {
        return repository.searchByTitle(title, pageable).map(this::toResponse);
    }

    @Override
    public Page<JobResponse> searchByTitleNotClosed(String title, Pageable pageable) {
        return repository.searchByTitleAndStatusNot(title, JobStatus.CLOSED, pageable).map(this::toResponse);
    }

    @Override
    public Page<JobResponse> searchByTitleAndCreatedBy(String title, Long createdBy, Pageable pageable) {
        return repository.searchByTitleAndCreatedBy(title, createdBy, pageable).map(this::toResponse);
    }

    @Override
    public Page<JobResponse> findByCategory(Long categoryId, Pageable pageable) {
        return repository.findByCategory(categoryId, pageable).map(this::toResponse);
    }

    @Override
    public Page<JobResponse> findByCategoryNotClosed(Long categoryId, Pageable pageable) {
        return repository.findByCategoryAndStatusNot(categoryId, JobStatus.CLOSED, pageable).map(this::toResponse);
    }

    @Override
    public Page<JobResponse> findByCategoryAndCreatedBy(Long categoryId, Long createdBy, Pageable pageable) {
        return repository.findByCategoryAndCreatedBy(categoryId, createdBy, pageable).map(this::toResponse);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
        auditLog.log(AuditAction.JOB_DELETED, AuditEntityType.JOB, id, "Xóa tin tuyển dụng #" + id);
    }

    // Recruiter chi duoc chon category dang mo; admin khong bi rang buoc nay.
    private void validateCategoryActive(Long categoryId) {
        if (categoryId == null) {
            return;
        }
        Category category = categoryRepository.findById(categoryId);
        if (!Boolean.TRUE.equals(category.getIsActive())) {
            throw new BusinessRuleException("Category is no longer available");
        }
    }

    private JobResponse updateStatus(Long id, JobStatus status) {
        Job job = repository.findById(id).job();
        JobStatus oldStatus = job.getStatus();
        job.setStatus(status);
        job.setUpdatedAt(Instant.now());
        JobResponse response = toResponse(repository.save(job));
        if (oldStatus != status) {
            auditLog.log(AuditAction.JOB_STATUS_CHANGED, AuditEntityType.JOB, id,
                    "Đổi trạng thái job #" + id + " từ " + oldStatus + " sang " + status,
                    oldStatus != null ? oldStatus.name() : null, status.name());
        }
        return response;
    }

    private JobResponse toResponse(JobView view) {
        JobResponse response = mapper.toResponse(view.job());
        if (view.company() != null) {
            response.setCompany(companyMapper.toResponse(view.company()));
        }
        if (view.createdBy() != null) {
            response.setCreatedBy(userMapper.toResponse(view.createdBy()));
        }
        if (view.category() != null) {
            response.setCategory(categoryMapper.toResponse(view.category()));
        }
        return response;
    }
}
