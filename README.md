# Applicant Tracking System

A small Spring Boot REST API demonstrating an Applicant Tracking System (ATS)
with JPA and Clean Architecture.

## Features

- Create and list candidates
- Create jobs and open/close them
- Apply a candidate to a job
- Move applications through the recruitment pipeline
- Prevent duplicate applications and applications to closed jobs
- MySQL 8 database
- Flyway-managed database schema

## Architecture

```text
adapter/in/web          REST controllers and request validation
        |
application/port/in     Use-case contracts
        |
application/service     Business workflows
        |
domain                  Plain Java business models and rules
        |
application/port/out    Repository contracts
        |
infrastructure          JPA entities, Spring Data and persistence adapters
```

The dependency direction points toward the domain. The domain does not know
about HTTP, Spring Data, or database tables.

## Database model

```text
candidates 1 ---- * applications * ---- 1 jobs
```

Each candidate may apply to many jobs, but only once per job.

## Run with MySQL

```bash
docker compose up -d
mvn spring-boot:run
```

The API runs at `http://localhost:8080`.

Default connection:

```text
Database: ats
Username: ats
Password: ats
Port:     3306
```

Override `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` when needed.

For an existing local MySQL installation, connect as an administrator in
MySQL Workbench and run [`database/setup_mysql.sql`](database/setup_mysql.sql)
once. Then start the application:

```bash
mvn spring-boot:run
```

If an older V1 migration was already executed, recreate the development
database so Flyway can apply the corrected schema:

```bash
docker compose down -v
docker compose up -d
mvn spring-boot:run
```

## Main endpoints

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/candidates` | Create candidate |
| GET | `/api/candidates` | List candidates |
| POST | `/api/jobs` | Create open job |
| GET | `/api/jobs` | List jobs |
| PATCH | `/api/jobs/{id}/status` | Open or close job |
| POST | `/api/applications` | Apply to a job |
| GET | `/api/applications` | List/filter applications |
| PATCH | `/api/applications/{id}/status` | Move recruitment stage |

Filter applications with `?candidateId=...` or `?jobId=...`.

Example candidate:

```json
{
  "fullName": "Lan Nguyen",
  "email": "lan@example.com",
  "phone": "0901234567",
  "address": "Ho Chi Minh City",
  "linkedinUrl": "https://linkedin.com/in/lan",
  "githubUrl": "https://github.com/lan",
  "portfolioUrl": "https://lan.dev",
  "currentPosition": "Java Fresher",
  "yearsOfExperience": 0
}
```

Application statuses: `APPLIED`, `SCREENING`, `INTERVIEW`, `OFFERED`,
`HIRED`, `REJECTED`, and `WITHDRAWN`.

## Test

```bash
mvn test
```
