package com.example.ats.infrastructure.persistence.adapter;

import com.example.ats.application.mapper.CategoryMapper;
import com.example.ats.application.mapper.CompanyMapper;
import com.example.ats.application.mapper.JobMapper;
import com.example.ats.application.mapper.UserMapper;
import com.example.ats.application.port.out.JobRepository;
import com.example.ats.domain.exception.ResourceNotFoundException;
import com.example.ats.domain.model.Category;
import com.example.ats.domain.model.Company;
import com.example.ats.domain.model.Job;
import com.example.ats.domain.model.JobStatus;
import com.example.ats.domain.model.User;
import com.example.ats.domain.view.JobView;
import com.example.ats.infrastructure.persistence.entity.CategoryEntity;
import com.example.ats.infrastructure.persistence.entity.CompanyEntity;
import com.example.ats.infrastructure.persistence.entity.JobEntity;
import com.example.ats.infrastructure.persistence.entity.UserEntity;
import com.example.ats.infrastructure.persistence.repository.SpringDataCategoryRepository;
import com.example.ats.infrastructure.persistence.repository.SpringDataCompanyRepository;
import com.example.ats.infrastructure.persistence.repository.SpringDataJobRepository;
import com.example.ats.infrastructure.persistence.repository.SpringDataUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class JobAdapter implements JobRepository {
    private final SpringDataJobRepository repository;
    private final SpringDataCompanyRepository companyRepository;
    private final SpringDataCategoryRepository categoryRepository;
    private final SpringDataUserRepository userRepository;
    private final JobMapper mapper;
    private final CompanyMapper companyMapper;
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;

    public JobAdapter(SpringDataJobRepository repository, SpringDataCompanyRepository companyRepository,
                      SpringDataCategoryRepository categoryRepository, SpringDataUserRepository userRepository,
                      JobMapper mapper, CompanyMapper companyMapper, CategoryMapper categoryMapper,
                      UserMapper userMapper) {
        this.repository = repository;
        this.companyRepository = companyRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.companyMapper = companyMapper;
        this.categoryMapper = categoryMapper;
        this.userMapper = userMapper;
    }

    @Override
    public JobView save(Job job) {
        CompanyEntity company = companyRepository.findById(job.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + job.getCompanyId()));
        CategoryEntity category = null;
        if (job.getCategoryId() != null) {
            category = categoryRepository.findById(job.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + job.getCategoryId()));
        }
        UserEntity createdBy = null;
        if (job.getCreatedBy() != null) {
            createdBy = userRepository.findById(job.getCreatedBy())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + job.getCreatedBy()));
        }
        JobEntity entity = new JobEntity(job.getId(), job.getTitle(), job.getDescription(), job.getRequirements(),
                job.getLocation(), job.getEmploymentType(), company, category, job.getSalaryMin(), job.getSalaryMax(),
                job.getStatus(), createdBy, job.getCreatedAt(), job.getUpdatedAt());
        return toView(repository.save(entity));
    }

    @Override
    public Page<JobView> findByCategory(Long categoryId, Pageable pageable) {
        return repository.findByCategory_Id(categoryId, pageable).map(this::toView);
    }

    @Override
    public Page<JobView> findByCategoryAndStatusNot(Long categoryId, JobStatus status, Pageable pageable) {
        return repository.findByCategory_IdAndStatusNot(categoryId, status, pageable).map(this::toView);
    }

    @Override
    public Page<JobView> findByCategoryAndCreatedBy(Long categoryId, Long createdBy, Pageable pageable) {
        return repository.findByCategory_IdAndCreatedBy_Id(categoryId, createdBy, pageable).map(this::toView);
    }

    @Override
    public JobView findById(Long id) {
        return toView(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found")));
    }

    @Override
    public Page<JobView> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toView);
    }

    @Override
    public Page<JobView> finByStatus(JobStatus status, Pageable pageable) {
        return repository.findByStatus(status, pageable).map(this::toView);
    }

    @Override
    public Page<JobView> findByStatusNot(JobStatus status, Pageable pageable) {
        return repository.findByStatusNot(status, pageable).map(this::toView);
    }

    @Override
    public Page<JobView> findByCreatedBy(Long createdBy, Pageable pageable) {
        return repository.findByCreatedBy_Id(createdBy, pageable).map(this::toView);
    }

    @Override
    public Page<JobView> searchByTitle(String title, Pageable pageable) {
        return repository.findByTitleContainingIgnoreCase(title, pageable).map(this::toView);
    }

    @Override
    public Page<JobView> searchByTitleAndStatusNot(String title, JobStatus status, Pageable pageable) {
        return repository.findByTitleContainingIgnoreCaseAndStatusNot(title, status, pageable).map(this::toView);
    }

    @Override
    public Page<JobView> searchByTitleAndCreatedBy(String title, Long createdBy, Pageable pageable) {
        return repository.findByTitleContainingIgnoreCaseAndCreatedBy_Id(title, createdBy, pageable).map(this::toView);
    }

    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Job not found");
        }
        repository.deleteById(id);
    }

    private JobView toView(JobEntity entity) {
        Job job = mapper.toEntity(entity);
        job.setCompanyId(entity.getCompany().getId());
        User createdBy = null;
        if (entity.getCreatedBy() != null) {
            job.setCreatedBy(entity.getCreatedBy().getId());
            createdBy = userMapper.toEntity(entity.getCreatedBy());
        }
        Category category = null;
        if (entity.getCategory() != null) {
            job.setCategoryId(entity.getCategory().getId());
            category = categoryMapper.toEntity(entity.getCategory());
        }
        Company company = companyMapper.toEntity(entity.getCompany());
        return new JobView(company, job, createdBy, category);
    }
}
