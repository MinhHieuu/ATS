# Applicant Tracking System

A Spring Boot REST API for managing the end-to-end recruitment process. The
system helps recruiters manage jobs, candidates, applications, interviews, and
recruitment metrics while giving candidates a place to discover jobs, apply,
and track their application status.

The project is built with JPA, MySQL, and Clean/Hexagonal Architecture.

## Features
### ADMIN
Administrators manage the entire system.

- User management
  - Create recruiter accounts.
  - Lock or unlock user accounts.
  - Assign roles:
    - ADMIN
    - RECRUITER
    - CANDIDATE
- Company management
  - Create companies.
  - Edit company information.
  - Activate or deactivate companies.
- Dashboard
  - Total number of candidates.
  - Total number of jobs.
  - Total number of recruiters.
  - Total number of hired candidates.
- Audit log
  - Track all actions, including:
    - A recruiter creating a job.
    - A recruiter changing a candidate's status.
    - A recruiter scheduling an interview.
### Recruiter

Recruiters are the primary users of the Applicant Tracking System.

- Create job postings.
- Edit job information.
- Open or close job postings.
- View and manage active and closed jobs.

#### Candidate management

- View a list of candidates with:
  - Full name
  - Email address
  - Phone number
  - CV/resume
  - GitHub profile
  - LinkedIn profile
- Search candidates by:
  - Name
  - Email address
  - Skills

#### Application management

- View applications for each job.
- Move applications between recruitment stages.
- Add recruiter notes.
- View the application change history.
- Track each candidate throughout the hiring pipeline.

#### Interview management

- Schedule interviews with:
  - Title
  - Date and time
  - Google Meet link
  - Physical location
  - Assigned interviewer
- Record feedback after an interview.
- Track interview status and results.

#### Recruitment analytics

- Number of candidates per job.
- Interview pass rate.
- Offer acceptance rate.
- Time-to-hire.

### Candidate

Candidates can create a profile, find suitable opportunities, apply for jobs,
and follow their progress through the recruitment process.

#### Authentication

- Register and sign in with email and password.
- Sign in with Google as an advanced authentication option.

#### Candidate profile

- Upload an avatar.
- Upload a CV/resume in PDF format.
- Add GitHub and LinkedIn profiles.
- Add work experience and professional information.

#### Job discovery

- Browse available job postings.
- Search and filter jobs by:
  - Keyword
  - Location
  - Salary
  - Technology

#### Job application

- Select or upload a CV.
- Write a cover letter.
- Apply for a job.
- Track the current application status.

#### Notifications

Candidates receive notifications when:

- They are invited to an interview.
- They receive an offer.
- Their application is rejected.

### Relationship Details

| Relationship | Cardinality | Foreign key | Description |
|---|---|---|---|
| Company to Recruiter | `1 - 0..N` | `recruiters.company_id` | A company can employ multiple recruiters; assigning a company to a recruiter is optional |
| User to Candidate | `1 - 0..1` | `candidates.user_id` | A candidate profile belongs to one user, and a user can have at most one candidate profile |
| User to Recruiter | `1 - 0..1` | `recruiters.user_id` | A recruiter profile belongs to one user, and a user can have at most one recruiter profile |
| User to Job | `1 - 0..N` | `jobs.created_by` | A user can create multiple jobs; assigning a creator to a job is optional |
| User to Activity Log | `1 - 0..N` | `activity_logs.user_id` | A user can perform multiple logged activities; the user reference is optional |
| Candidate to Resume | `1 - 0..N` | `resumes.candidate_id` | A candidate can own multiple resumes, and every resume must belong to one candidate |
| Candidate to Application | `1 - 0..N` | `applications.candidate_id` | A candidate can submit multiple applications, and every application must belong to one candidate |
| Job to Application | `1 - 0..N` | `applications.job_id` | A job can receive multiple applications, and every application must target one job |
| Application Stage to Application | `1 - 0..N` | `applications.stage_id` | A stage can classify multiple applications; assigning a stage is optional |
| Application to Interview | `1 - 0..N` | `interviews.application_id` | An application can have multiple interviews, and every interview must belong to one application |
| Recruiter to Interview | `1 - 0..N` | `interviews.interviewer_id` | A recruiter can conduct multiple interviews; assigning an interviewer is optional |
| Interview to Feedback | `1 - 0..N` | `interview_feedbacks.interview_id` | An interview can receive multiple feedback entries, and each entry belongs to one interview |
| Recruiter to Feedback | `1 - 0..N` | `interview_feedbacks.reviewer_id` | A recruiter can provide multiple feedback entries, and every entry must have one reviewer |

The `applications` table is the junction between `candidates` and `jobs`. It
resolves their many-to-many relationship into two one-to-many relationships
and stores each application's current recruitment stage.

The API starts at `http://localhost:8080`.


