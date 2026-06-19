CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    avatar_url VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
        ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uk_users_email UNIQUE (email)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE roles (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    CONSTRAINT pk_roles PRIMARY KEY (id),
    CONSTRAINT uk_roles_name UNIQUE (name)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;


CREATE TABLE candidates (
    id BIGINT NOT NULL AUTO_INCREMENT,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(255),
    linkedin_url VARCHAR(255),
    github_url VARCHAR(255),
    portfolio_url VARCHAR(255),
    current_position VARCHAR(100),
    years_of_experience INT NOT NULL DEFAULT 0,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
        ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT pk_candidates PRIMARY KEY (id),
    CONSTRAINT uk_candidates_email UNIQUE (email),
    CONSTRAINT chk_candidates_experience CHECK (years_of_experience >= 0)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE jobs (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    requirements TEXT,
    location VARCHAR(100),
    employment_type VARCHAR(50),
    salary_min DECIMAL(12, 2),
    salary_max DECIMAL(12, 2),
    status VARCHAR(50) NOT NULL DEFAULT 'OPEN',
    created_by BIGINT,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
        ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT pk_jobs PRIMARY KEY (id),
    CONSTRAINT fk_jobs_created_by FOREIGN KEY (created_by)
        REFERENCES users (id) ON DELETE SET NULL,
    CONSTRAINT chk_jobs_salary CHECK (
        salary_min IS NULL OR salary_max IS NULL OR salary_min <= salary_max
    )
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE application_stages (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    position INT NOT NULL,
    CONSTRAINT pk_application_stages PRIMARY KEY (id),
    CONSTRAINT uk_application_stages_name UNIQUE (name),
    CONSTRAINT uk_application_stages_position UNIQUE (position)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE applications (
    id BIGINT NOT NULL AUTO_INCREMENT,
    candidate_id BIGINT NOT NULL,
    job_id BIGINT NOT NULL,
    stage_id BIGINT,
    status VARCHAR(50) NOT NULL DEFAULT 'APPLIED',
    source VARCHAR(100),
    expected_salary DECIMAL(12, 2),
    note TEXT,
    applied_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
        ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT pk_applications PRIMARY KEY (id),
    CONSTRAINT uk_candidate_job UNIQUE (candidate_id, job_id),
    CONSTRAINT fk_applications_candidate FOREIGN KEY (candidate_id)
        REFERENCES candidates (id) ON DELETE RESTRICT,
    CONSTRAINT fk_applications_job FOREIGN KEY (job_id)
        REFERENCES jobs (id) ON DELETE RESTRICT,
    CONSTRAINT fk_applications_stage FOREIGN KEY (stage_id)
        REFERENCES application_stages (id) ON DELETE SET NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE resumes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    candidate_id BIGINT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_url VARCHAR(255) NOT NULL,
    uploaded_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    CONSTRAINT pk_resumes PRIMARY KEY (id),
    CONSTRAINT fk_resumes_candidate FOREIGN KEY (candidate_id)
        REFERENCES candidates (id) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE interviews (
    id BIGINT NOT NULL AUTO_INCREMENT,
    application_id BIGINT NOT NULL,
    interviewer_id BIGINT,
    title VARCHAR(150),
    interview_time DATETIME(6) NOT NULL,
    meeting_link VARCHAR(255),
    location VARCHAR(255),
    status VARCHAR(50) NOT NULL DEFAULT 'SCHEDULED',
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    CONSTRAINT pk_interviews PRIMARY KEY (id),
    CONSTRAINT fk_interviews_application FOREIGN KEY (application_id)
        REFERENCES applications (id) ON DELETE CASCADE,
    CONSTRAINT fk_interviews_interviewer FOREIGN KEY (interviewer_id)
        REFERENCES users (id) ON DELETE SET NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE interview_feedbacks (
    id BIGINT NOT NULL AUTO_INCREMENT,
    interview_id BIGINT NOT NULL,
    reviewer_id BIGINT NOT NULL,
    rating INT,
    comment TEXT,
    recommendation VARCHAR(50),
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    CONSTRAINT pk_interview_feedbacks PRIMARY KEY (id),
    CONSTRAINT fk_feedbacks_interview FOREIGN KEY (interview_id)
        REFERENCES interviews (id) ON DELETE CASCADE,
    CONSTRAINT fk_feedbacks_reviewer FOREIGN KEY (reviewer_id)
        REFERENCES users (id) ON DELETE RESTRICT,
    CONSTRAINT chk_feedbacks_rating CHECK (rating IS NULL OR rating BETWEEN 1 AND 5)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE activity_logs (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT,
    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(100),
    entity_id BIGINT,
    description TEXT,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    CONSTRAINT pk_activity_logs PRIMARY KEY (id),
    CONSTRAINT fk_activity_logs_user FOREIGN KEY (user_id)
        REFERENCES users (id) ON DELETE SET NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE INDEX idx_jobs_created_by ON jobs (created_by);
CREATE INDEX idx_applications_candidate ON applications (candidate_id);
CREATE INDEX idx_applications_job ON applications (job_id);
CREATE INDEX idx_applications_stage ON applications (stage_id);
CREATE INDEX idx_applications_status ON applications (status);
CREATE INDEX idx_resumes_candidate ON resumes (candidate_id);
CREATE INDEX idx_interviews_application ON interviews (application_id);
CREATE INDEX idx_interviews_interviewer ON interviews (interviewer_id);
CREATE INDEX idx_feedbacks_interview ON interview_feedbacks (interview_id);
CREATE INDEX idx_feedbacks_reviewer ON interview_feedbacks (reviewer_id);
CREATE INDEX idx_activity_logs_user ON activity_logs (user_id);
CREATE INDEX idx_activity_logs_entity ON activity_logs (entity_type, entity_id);

INSERT INTO roles (name) VALUES ('ADMIN'), ('RECRUITER'), ('INTERVIEWER');

INSERT INTO application_stages (name, position) VALUES
    ('Applied', 1),
    ('Screening', 2),
    ('Interview', 3),
    ('Offer', 4),
    ('Hired', 5),
    ('Rejected', 6);
