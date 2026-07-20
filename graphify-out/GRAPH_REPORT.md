# Graph Report - .  (2026-07-19)

## Corpus Check
- Corpus is ~23,214 words - fits in a single context window. You may not need a graph.

## Summary
- 1016 nodes · 2678 edges · 32 communities (28 shown, 4 thin omitted)
- Extraction: 93% EXTRACTED · 7% INFERRED · 0% AMBIGUOUS · INFERRED: 179 edges (avg confidence: 0.8)
- Token cost: 110,576 input · 0 output

## Community Hubs (Navigation)
- Admin Job Controller
- Company & Job Mapping/Repos
- Candidate Controller
- Job Application Mapping/Repos
- Recruiter Controller
- Admin Company Controller
- User Entity & Repositories
- Resume Controller
- Project Docs & Architecture
- Global Exception Handling & Config
- User Request/Response DTOs
- Auth Controller
- JWT Service
- Admin Application Controller
- Job Application Service
- Candidate Application Controller
- Admin User Controller
- Security Configuration
- Recruiter Application Controller
- Application Status Workflow
- Admin User Management
- Auth Service & User Mapper
- Maven Wrapper Script
- User Service Tests
- Application Flow Integration Tests
- CI/CD & Docker Config
- API Response Serialization Tests
- Spring Boot Application Entry
- Maven Project Coordinates
- Server Port Config
- Upload Directory Config

## God Nodes (most connected - your core abstractions)
1. `ApiResponse` - 85 edges
2. `JobResponse` - 56 edges
3. `JobApplicationResponse` - 53 edges
4. `CompanyResponse` - 41 edges
5. `UserResponse` - 41 edges
6. `ResourceNotFoundException` - 40 edges
7. `CandidateResponse` - 30 edges
8. `UserEntity` - 29 edges
9. `JobApplicationService` - 28 edges
10. `User` - 28 edges

## Surprising Connections (you probably didn't know these)
- `Applications Junction Table (Candidate x Job)` --semantically_similar_to--> `JobApplicationResponse Schema`  [INFERRED] [semantically similar]
  README.md → DOCUMENT_API.md
- `Candidate Apply Workflow (business.md)` --semantically_similar_to--> `Application Status State Machine`  [INFERRED] [semantically similar]
  business.md → DOCUMENT_API.md
- `Auth Workflow (business.md)` --semantically_similar_to--> `JWT Authentication (access/refresh token)`  [INFERRED] [semantically similar]
  business.md → DOCUMENT_API.md
- `Candidate Features (README)` --semantically_similar_to--> `Candidate API Endpoints (/api/candidates)`  [INFERRED] [semantically similar]
  README.md → DOCUMENT_API.md
- `Candidate Features (README)` --semantically_similar_to--> `Resume API Endpoints (/api/resumes)`  [INFERRED] [semantically similar]
  README.md → DOCUMENT_API.md

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **MySQL Infrastructure Across Environments (CI, Local, Production)** — _github_workflows_ci_mysql_service, docker_compose_mysql_service, src_main_resources_application_datasource [INFERRED 0.85]
- **Authentication Flow (JWT Issue/Validate/Refresh)** — business_auth_workflow, document_api_jwt_auth, src_main_resources_application_jwt_config, document_api_auth_endpoints [INFERRED 0.85]
- **Candidate Application Lifecycle** — business_candidate_apply_workflow, document_api_application_state_machine, document_api_applicationstatus_enum, readme_applications_junction_table [INFERRED 0.85]

## Communities (32 total, 4 thin omitted)

### Community 0 - "Admin Job Controller"
Cohesion: 0.06
Nodes (40): Jwt, AdminJobController, Authentication, GetMapping, PatchMapping, PostMapping, RequestMapping, ResponseEntity (+32 more)

### Community 1 - "Company & Job Mapping/Repos"
Cohesion: 0.06
Nodes (45): CompanyRepository, JobRepository, Mapper, Mapping, JobMapper, Company, AllArgsConstructor, Getter (+37 more)

### Community 2 - "Candidate Controller"
Cohesion: 0.05
Nodes (42): CandidateRepository, CandidateController, Authentication, GetMapping, PatchMapping, PostMapping, RequestMapping, ResponseEntity (+34 more)

### Community 3 - "Job Application Mapping/Repos"
Cohesion: 0.06
Nodes (37): JobApplicationRepository, ResumeRepository, Mapper, Mapping, JobApplicationMapper, ResourceNotFoundException, AllArgsConstructor, Getter (+29 more)

### Community 4 - "Recruiter Controller"
Cohesion: 0.06
Nodes (41): RecruiterRepository, Authentication, GetMapping, PatchMapping, RequestMapping, ResponseEntity, RestController, RecruiterController (+33 more)

### Community 5 - "Admin Company Controller"
Cohesion: 0.08
Nodes (28): AdminCompanyController, GetMapping, PatchMapping, PostMapping, RequestMapping, ResponseEntity, RestController, CompanyController (+20 more)

### Community 6 - "User Entity & Repositories"
Cohesion: 0.06
Nodes (33): JpaRepository, RefreshTokenRepository, AllArgsConstructor, Getter, Setter, User, Override, Repository (+25 more)

### Community 7 - "Resume Controller"
Cohesion: 0.09
Nodes (24): Component, DeleteMapping, GetMapping, PatchMapping, PostMapping, RequestMapping, ResponseEntity, RestController (+16 more)

### Community 8 - "Project Docs & Architecture"
Cohesion: 0.06
Nodes (50): Auth Workflow (business.md), Candidate Apply Workflow (business.md), Clean/Hexagonal Architecture, ATS Project (CLAUDE.md), Graphify Usage Rules, Package Structure, Development Rules, Technology Stack (+42 more)

### Community 9 - "Global Exception Handling & Config"
Cohesion: 0.09
Nodes (26): ConfigurationProperties, EnableConfigurationProperties, ExceptionHandler, HttpMessageNotReadableException, HttpStatus, MethodArgumentNotValidException, ResourceHandlerRegistry, RestControllerAdvice (+18 more)

### Community 10 - "User Request/Response DTOs"
Cohesion: 0.16
Nodes (15): Pattern, Getter, Setter, UserRequest, AllArgsConstructor, Getter, NoArgsConstructor, Setter (+7 more)

### Community 11 - "Auth Controller"
Cohesion: 0.13
Nodes (18): AuthController, PostMapping, RequestMapping, ResponseEntity, RestController, Getter, LoginRequest, Getter (+10 more)

### Community 12 - "JWT Service"
Cohesion: 0.14
Nodes (10): Claims, Key, Override, Service, JWTService, BusinessRuleException, Role, ADMIN (+2 more)

### Community 13 - "Admin Application Controller"
Cohesion: 0.15
Nodes (12): AdminApplicationController, DeleteMapping, GetMapping, PatchMapping, RequestMapping, ResponseEntity, RestController, AllArgsConstructor (+4 more)

### Community 14 - "Job Application Service"
Cohesion: 0.20
Nodes (7): JobApplicationRepository, JobRepository, Override, ResumeRepository, Service, Transactional, JobApplicationService

### Community 15 - "Candidate Application Controller"
Cohesion: 0.16
Nodes (11): ApplicationController, Authentication, GetMapping, PatchMapping, PostMapping, RequestMapping, ResponseEntity, RestController (+3 more)

### Community 16 - "Admin User Controller"
Cohesion: 0.14
Nodes (10): Authentication, PatchMapping, RequestMapping, ResponseEntity, RestController, UserController, ChangePasswordRequest, Getter (+2 more)

### Community 17 - "Security Configuration"
Cohesion: 0.21
Nodes (12): AbstractAuthenticationToken, Bean, Converter, CorsConfigurationSource, CORS Configuration, EnableMethodSecurity, HttpSecurity, JwtDecoder (+4 more)

### Community 18 - "Recruiter Application Controller"
Cohesion: 0.24
Nodes (8): Authentication, GetMapping, PatchMapping, RequestMapping, ResponseEntity, RestController, RecruiterApplicationController, JobApplicationUseCase

### Community 19 - "Application Status Workflow"
Cohesion: 0.16
Nodes (11): ApplicationStatusRequest, Getter, Setter, ApplicationStatus, APPLICATION_CREATED, HIRED, INTERVIEW, OFFER (+3 more)

### Community 20 - "Admin User Management"
Cohesion: 0.32
Nodes (7): AdminUserController, Authentication, GetMapping, PatchMapping, RequestMapping, ResponseEntity, RestController

### Community 21 - "Auth Service & User Mapper"
Cohesion: 0.38
Nodes (8): Mapper, UserMapper, AuthService, PasswordEncoder, RefreshTokenRepository, Service, Transactional, UserRepository

### Community 22 - "Maven Wrapper Script"
Cohesion: 0.33
Nodes (6): mvnw script, clean(), die(), exec_maven(), set_java_home(), verbose()

### Community 23 - "User Service Tests"
Cohesion: 0.36
Nodes (6): ExtendWith, ObjectMapper, PasswordEncoder, Test, UserRepository, UserServiceTest

### Community 24 - "Application Flow Integration Tests"
Cohesion: 0.43
Nodes (6): AutoConfigureMockMvc, MockMvc, SpringBootTest, ApplicationFlowIntegrationTest, ObjectMapper, Test

### Community 25 - "CI/CD & Docker Config"
Cohesion: 0.60
Nodes (5): CI Build Job (mvn clean package -DskipTests), Java CI with Maven Workflow, CI MySQL Service (mysql:8), Docker Compose MySQL Service (mysql:8.4), Spring Datasource Config

### Community 26 - "API Response Serialization Tests"
Cohesion: 0.60
Nodes (3): ApiResponseSerializationTest, ObjectMapper, Test

## Ambiguous Edges - Review These
- `Development Rules` → `DOCUMENT_API.md — ATS REST API Documentation`  [AMBIGUOUS]
  CLAUDE.md · relation: references

## Knowledge Gaps
- **28 isolated node(s):** `com.example:ats`, `APPLICATION_CREATED`, `SCREENING`, `INTERVIEW`, `OFFER` (+23 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **4 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **What is the exact relationship between `Development Rules` and `DOCUMENT_API.md — ATS REST API Documentation`?**
  _Edge tagged AMBIGUOUS (relation: references) - confidence is low._
- **Why does `ApiResponse` connect `Admin Job Controller` to `Candidate Controller`, `Recruiter Controller`, `Admin Company Controller`, `Resume Controller`, `Global Exception Handling & Config`, `Auth Controller`, `Admin Application Controller`, `Candidate Application Controller`, `Admin User Controller`, `Recruiter Application Controller`, `Admin User Management`, `API Response Serialization Tests`?**
  _High betweenness centrality (0.252) - this node is a cross-community bridge._
- **Why does `ResourceNotFoundException` connect `Job Application Mapping/Repos` to `Admin Job Controller`, `Company & Job Mapping/Repos`, `Candidate Controller`, `Recruiter Controller`, `Admin Company Controller`, `User Entity & Repositories`, `Global Exception Handling & Config`, `JWT Service`, `Job Application Service`, `Auth Service & User Mapper`?**
  _High betweenness centrality (0.173) - this node is a cross-community bridge._
- **Why does `DOCUMENT_API.md — ATS REST API Documentation` connect `Project Docs & Architecture` to `Security Configuration`?**
  _High betweenness centrality (0.108) - this node is a cross-community bridge._
- **What connects `com.example:ats`, `APPLICATION_CREATED`, `SCREENING` to the rest of the system?**
  _28 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Admin Job Controller` be split into smaller, more focused modules?**
  _Cohesion score 0.055043859649122805 - nodes in this community are weakly interconnected._
- **Should `Company & Job Mapping/Repos` be split into smaller, more focused modules?**
  _Cohesion score 0.055642633228840124 - nodes in this community are weakly interconnected._