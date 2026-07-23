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


The API starts at `http://localhost:8080`.


