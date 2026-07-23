# Graph Report - ATS  (2026-07-22)

## Corpus Check
- 160 files · ~34,617 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 1588 nodes · 4402 edges · 66 communities (62 shown, 4 thin omitted)
- Extraction: 94% EXTRACTED · 6% INFERRED · 0% AMBIGUOUS · INFERRED: 253 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `c9908d5d`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

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
- InterviewFeedbackResponse
- ResourceNotFoundException
- InterviewFeedbackEntity
- Company
- InterviewResponse
- InterviewService
- GROUP API — ATS (Applicant Tracking System)
- Notification
- Candidate
- RecruiterAdapter.java
- CandidateRequest
- RecruiterResponse
- AdminInterviewController.java
- NotificationUseCase
- JobApplicationResponse
- RecruiterInterviewController.java
- NotificationService
- NotificationType
- JpaRepository
- NotificationEntity
- CandidateEntity
- InterviewUseCase
- InterviewController.java
- WebSocketAuthInterceptor
- WebSocketConfig
- Candidate Application API Endpoints (/api/applications)
- ATS Project (CLAUDE.md)
- UserResponse Schema
- PageableConfig.java
- StompPrincipal
- JobService.java
- JWT Authentication (access/refresh token)
- NotificationEntity
- JobApplicationResponse

## God Nodes (most connected - your core abstractions)
1. `ApiResponse` - 116 edges
2. `InterviewResponse` - 59 edges
3. `JobResponse` - 58 edges
4. `JobApplicationResponse` - 56 edges
5. `ResourceNotFoundException` - 54 edges
6. `UserResponse` - 42 edges
7. `CompanyResponse` - 41 edges
8. `JobApplicationService` - 40 edges
9. `UserMapper` - 36 edges
10. `InterviewFeedbackResponse` - 35 edges

## Surprising Connections (you probably didn't know these)
- `Candidate Apply Workflow (business.md)` --semantically_similar_to--> `Application Status State Machine`  [INFERRED] [semantically similar]
  business.md → DOCUMENT_API.md
- `Applications Junction Table (Candidate x Job)` --semantically_similar_to--> `JobApplicationResponse Schema`  [INFERRED] [semantically similar]
  README.md → DOCUMENT_API.md
- `Candidate Features (README)` --semantically_similar_to--> `Candidate API Endpoints (/api/candidates)`  [INFERRED] [semantically similar]
  README.md → DOCUMENT_API.md
- `Recruiter Features (README)` --semantically_similar_to--> `Recruiter Job API Endpoints (/api/recruiter/jobs)`  [INFERRED] [semantically similar]
  README.md → DOCUMENT_API.md
- `Development Rules` --references--> `DOCUMENT_API.md — ATS REST API Documentation`  [AMBIGUOUS]
  CLAUDE.md → DOCUMENT_API.md

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **MySQL Infrastructure Across Environments (CI, Local, Production)** — _github_workflows_ci_mysql_service, docker_compose_mysql_service, src_main_resources_application_datasource [INFERRED 0.85]
- **Authentication Flow (JWT Issue/Validate/Refresh)** — business_auth_workflow, document_api_jwt_auth, src_main_resources_application_jwt_config, document_api_auth_endpoints [INFERRED 0.85]
- **Candidate Application Lifecycle** — business_candidate_apply_workflow, document_api_application_state_machine, document_api_applicationstatus_enum, readme_applications_junction_table [INFERRED 0.85]

## Communities (66 total, 4 thin omitted)

### Community 0 - "Admin Job Controller"
Cohesion: 0.06
Nodes (45): AdminJobController, Authentication, GetMapping, Page, Pageable, PatchMapping, PostMapping, RequestMapping (+37 more)

### Community 1 - "Company & Job Mapping/Repos"
Cohesion: 0.05
Nodes (54): CompanyRepository, JobRepository, Mapper, Mapper, Mapping, Company, AllArgsConstructor, Getter (+46 more)

### Community 2 - "Candidate Controller"
Cohesion: 0.21
Nodes (10): CandidateController, Authentication, GetMapping, Page, Pageable, PatchMapping, RequestMapping, ResponseEntity (+2 more)

### Community 3 - "Job Application Mapping/Repos"
Cohesion: 0.12
Nodes (23): JpaRepository, ResumeRepository, Component, ResumeMapper, AllArgsConstructor, Getter, Setter, Resume (+15 more)

### Community 4 - "Recruiter Controller"
Cohesion: 0.05
Nodes (52): RecruiterRepository, Authentication, GetMapping, Page, Pageable, PatchMapping, RequestMapping, ResponseEntity (+44 more)

### Community 5 - "Admin Company Controller"
Cohesion: 0.07
Nodes (41): AdminCompanyController, GetMapping, Page, Pageable, PatchMapping, PostMapping, RequestMapping, ResponseEntity (+33 more)

### Community 6 - "User Entity & Repositories"
Cohesion: 0.05
Nodes (44): ExtendWith, RefreshTokenRepository, Mapper, AllArgsConstructor, Getter, Setter, User, Override (+36 more)

### Community 7 - "Resume Controller"
Cohesion: 0.09
Nodes (28): DeleteMapping, GetMapping, Page, Pageable, PatchMapping, PostMapping, RequestMapping, ResponseEntity (+20 more)

### Community 8 - "Project Docs & Architecture"
Cohesion: 0.18
Nodes (17): Admin Company API Endpoints (/api/admin/companies), Admin Job API Endpoints (/api/admin/jobs), Admin User API Endpoints (/api/admin/users), ApiError Error Envelope, ApiResponse<T> Success Envelope, Public Company API Endpoints (/api/companies), CompanyResponse Schema, EmploymentType Enum (FULLTIME/PARTTIME) (+9 more)

### Community 9 - "Global Exception Handling & Config"
Cohesion: 0.09
Nodes (27): ConfigurationProperties, DataIntegrityViolationException, EnableConfigurationProperties, ExceptionHandler, HttpMessageNotReadableException, HttpStatus, MethodArgumentNotValidException, ResourceHandlerRegistry (+19 more)

### Community 10 - "User Request/Response DTOs"
Cohesion: 0.14
Nodes (17): Pattern, Getter, Setter, UserRequest, AllArgsConstructor, Getter, NoArgsConstructor, Setter (+9 more)

### Community 11 - "Auth Controller"
Cohesion: 0.39
Nodes (4): PostMapping, CandidateRequest, Getter, Setter

### Community 12 - "JWT Service"
Cohesion: 0.15
Nodes (10): Claims, Key, Override, Service, JWTService, BusinessRuleException, Role, ADMIN (+2 more)

### Community 13 - "Admin Application Controller"
Cohesion: 0.21
Nodes (9): AdminApplicationController, DeleteMapping, GetMapping, Page, Pageable, PatchMapping, RequestMapping, ResponseEntity (+1 more)

### Community 14 - "Job Application Service"
Cohesion: 0.15
Nodes (11): NotificationUseCase, InterviewRepository, JobApplicationRepository, JobRepository, Override, Page, Pageable, ResumeRepository (+3 more)

### Community 15 - "Candidate Application Controller"
Cohesion: 0.17
Nodes (13): ApplicationController, Authentication, GetMapping, Page, Pageable, PatchMapping, PostMapping, RequestMapping (+5 more)

### Community 16 - "Admin User Controller"
Cohesion: 0.21
Nodes (9): Authentication, PatchMapping, RequestMapping, ResponseEntity, RestController, UserController, ChangePasswordRequest, Getter (+1 more)

### Community 17 - "Security Configuration"
Cohesion: 0.20
Nodes (13): AbstractAuthenticationToken, Converter, CorsConfigurationSource, CORS Configuration, EnableMethodSecurity, HttpSecurity, Jwt, JwtDecoder (+5 more)

### Community 18 - "Recruiter Application Controller"
Cohesion: 0.25
Nodes (9): Authentication, GetMapping, Page, Pageable, PatchMapping, RequestMapping, ResponseEntity, RestController (+1 more)

### Community 19 - "Application Status Workflow"
Cohesion: 0.19
Nodes (11): ApplicationStatusRequest, Getter, Setter, ApplicationStatus, APPLICATION_CREATED, HIRED, INTERVIEW, OFFER (+3 more)

### Community 20 - "Admin User Management"
Cohesion: 0.17
Nodes (12): AdminUserController, Authentication, GetMapping, Page, Pageable, PatchMapping, RequestMapping, ResponseEntity (+4 more)

### Community 21 - "Auth Service & User Mapper"
Cohesion: 0.54
Nodes (6): AuthService, PasswordEncoder, RefreshTokenRepository, Service, Transactional, UserRepository

### Community 22 - "Maven Wrapper Script"
Cohesion: 0.33
Nodes (6): mvnw script, clean(), die(), exec_maven(), set_java_home(), verbose()

### Community 23 - "User Service Tests"
Cohesion: 0.21
Nodes (13): ApplicationEntity, AllArgsConstructor, Entity, Getter, NoArgsConstructor, Setter, Table, EntityGraph (+5 more)

### Community 24 - "Application Flow Integration Tests"
Cohesion: 0.43
Nodes (6): AutoConfigureMockMvc, MockMvc, SpringBootTest, ApplicationFlowIntegrationTest, ObjectMapper, Test

### Community 25 - "CI/CD & Docker Config"
Cohesion: 0.60
Nodes (5): CI Build Job (mvn clean package -DskipTests), Java CI with Maven Workflow, CI MySQL Service (mysql:8), Docker Compose MySQL Service (mysql:8.4), Spring Datasource Config

### Community 26 - "API Response Serialization Tests"
Cohesion: 0.18
Nodes (11): AuthController, PostMapping, RequestMapping, ResponseEntity, RestController, Getter, RefreshTokenRequest, AccessTokenResponse (+3 more)

### Community 32 - "InterviewFeedbackResponse"
Cohesion: 0.06
Nodes (44): AdminInterviewFeedbackController, DeleteMapping, GetMapping, Page, Pageable, RequestMapping, ResponseEntity, RestController (+36 more)

### Community 33 - "ResourceNotFoundException"
Cohesion: 0.09
Nodes (31): InterviewRepository, ResourceNotFoundException, Interview, AllArgsConstructor, Getter, Setter, InterviewResult, CANCELLED (+23 more)

### Community 34 - "InterviewFeedbackEntity"
Cohesion: 0.11
Nodes (24): InterviewFeedbackRepository, InterviewFeedback, AllArgsConstructor, Getter, Setter, InterviewFeedbackView, InterviewFeedbackAdapter, Override (+16 more)

### Community 35 - "Company"
Cohesion: 0.35
Nodes (5): JobApplicationView, Override, Page, Pageable, JobApplicationAdapter

### Community 36 - "InterviewResponse"
Cohesion: 0.17
Nodes (6): InterviewRequest, Getter, Setter, InterviewType, OFFLINE, ONLINE

### Community 37 - "InterviewService"
Cohesion: 0.19
Nodes (8): InterviewService, InterviewRepository, JobApplicationRepository, Override, Page, Pageable, Service, Transactional

### Community 38 - "GROUP API — ATS (Applicant Tracking System)"
Cohesion: 0.08
Nodes (23): 10. Admin Job — `/api/admin/jobs`, 11. Admin Company — `/api/admin/companies`, 12. Application — Candidate — `/api/applications`, 13. Application — Recruiter — `/api/recruiter/applications`, 14. Application — Admin — `/api/admin/applications`, 15. Admin User — `/api/admin/users`, 16. Notification — `/api/notifications`, 17. Interview — Candidate — `/api/interviews` (+15 more)

### Community 39 - "Notification"
Cohesion: 0.16
Nodes (14): NotificationRepository, Mapper, Mapping, NotificationMapper, AllArgsConstructor, Getter, Setter, Notification (+6 more)

### Community 40 - "Candidate"
Cohesion: 0.18
Nodes (8): Override, NotificationType, APPLICATION_RECEIVED, APPLICATION_STATUS_CHANGED, APPLICATION_WITHDRAWN, INTERVIEW_SCHEDULED, INTERVIEW_UPDATED, JOB_CREATED

### Community 41 - "RecruiterAdapter.java"
Cohesion: 0.29
Nodes (8): ChannelRegistration, EnableWebSocketMessageBroker, MessageBrokerRegistry, Configuration, Override, WebSocketConfig, StompEndpointRegistry, WebSocketMessageBrokerConfigurer

### Community 42 - "CandidateRequest"
Cohesion: 0.15
Nodes (13): Mapper, CandidateService, CandidateRepository, Override, Page, Pageable, Service, Transactional (+5 more)

### Community 43 - "RecruiterResponse"
Cohesion: 0.31
Nodes (7): Getter, LoginRequest, AllArgsConstructor, Getter, NoArgsConstructor, Setter, LoginResponse

### Community 44 - "AdminInterviewController.java"
Cohesion: 0.21
Nodes (10): AdminInterviewController, DeleteMapping, GetMapping, Page, Pageable, PatchMapping, PostMapping, RequestMapping (+2 more)

### Community 45 - "NotificationUseCase"
Cohesion: 0.22
Nodes (9): Authentication, GetMapping, Page, Pageable, PatchMapping, RequestMapping, ResponseEntity, RestController (+1 more)

### Community 46 - "JobApplicationResponse"
Cohesion: 0.47
Nodes (3): Page, Pageable, JobApplicationUseCase

### Community 47 - "RecruiterInterviewController.java"
Cohesion: 0.26
Nodes (11): Authentication, DeleteMapping, GetMapping, Page, Pageable, PatchMapping, PostMapping, RequestMapping (+3 more)

### Community 48 - "NotificationService"
Cohesion: 0.35
Nodes (8): SimpMessagingTemplate, NotificationRepository, Page, Pageable, Service, Transactional, UserRepository, NotificationService

### Community 49 - "NotificationType"
Cohesion: 0.29
Nodes (7): AllArgsConstructor, Getter, NoArgsConstructor, Setter, NotificationResponse, Page, Pageable

### Community 50 - "JpaRepository"
Cohesion: 0.29
Nodes (7): Mapper, Mapping, JobApplicationMapper, AllArgsConstructor, Getter, Setter, JobApplication

### Community 51 - "NotificationEntity"
Cohesion: 0.39
Nodes (5): InterviewResponse, AllArgsConstructor, Getter, NoArgsConstructor, Setter

### Community 52 - "CandidateEntity"
Cohesion: 0.13
Nodes (17): CandidateRepository, CandidateAdapter, Override, Page, Pageable, Repository, CandidateEntity, AllArgsConstructor (+9 more)

### Community 53 - "InterviewUseCase"
Cohesion: 0.20
Nodes (6): InterviewResultRequest, Getter, Setter, InterviewUseCase, Page, Pageable

### Community 54 - "InterviewController.java"
Cohesion: 0.28
Nodes (8): InterviewController, Authentication, GetMapping, Page, Pageable, RequestMapping, ResponseEntity, RestController

### Community 55 - "WebSocketAuthInterceptor"
Cohesion: 0.26
Nodes (7): ChannelInterceptor, Message, MessageChannel, Component, Override, WebSocketAuthInterceptor, StompHeaderAccessor

### Community 56 - "WebSocketConfig"
Cohesion: 0.23
Nodes (7): CandidateResponse, AllArgsConstructor, Getter, NoArgsConstructor, Setter, Page, Pageable

### Community 60 - "Candidate Application API Endpoints (/api/applications)"
Cohesion: 0.21
Nodes (14): Admin Application API Endpoints (/api/admin/applications), Candidate Application API Endpoints (/api/applications), Application Status State Machine, ApplicationStatus Enum, JobApplicationResponse Schema, Ownership Access Returns 404 Not 403, Recruiter Application API Endpoints (/api/recruiter/applications), Resume API Endpoints (/api/resumes) (+6 more)

### Community 61 - "ATS Project (CLAUDE.md)"
Cohesion: 0.33
Nodes (7): Clean/Hexagonal Architecture, ATS Project (CLAUDE.md), Graphify Usage Rules, Package Structure, Development Rules, Technology Stack, No Automated Testing Rule

### Community 62 - "UserResponse Schema"
Cohesion: 0.33
Nodes (7): AccessTokenResponse Schema, Auth API Endpoints (/api/auth), Candidate API Endpoints (/api/candidates), CandidateResponse Schema, LoginResponse Schema, User API Endpoints (/api/users), UserResponse Schema

### Community 63 - "PageableConfig.java"
Cohesion: 0.48
Nodes (5): EnableSpringDataWebSupport, PageableHandlerMethodArgumentResolverCustomizer, Bean, Configuration, PageableConfig

### Community 64 - "StompPrincipal"
Cohesion: 0.40
Nodes (3): Principal, Override, StompPrincipal

### Community 65 - "JobService.java"
Cohesion: 0.33
Nodes (6): JobApplicationRepository, CandidateMapper, CompanyMapper, JobMapper, UserMapper, Repository

### Community 66 - "JWT Authentication (access/refresh token)"
Cohesion: 0.40
Nodes (5): Auth Workflow (business.md), Candidate Apply Workflow (business.md), JWT Authentication (access/refresh token), Role Enum (ADMIN/RECRUITER/CANDIDATE), JWT Config (secret-key, access/refresh expiration)

### Community 68 - "NotificationEntity"
Cohesion: 0.20
Nodes (11): Modifying, AllArgsConstructor, Entity, Getter, NoArgsConstructor, Setter, Table, NotificationEntity (+3 more)

### Community 69 - "JobApplicationResponse"
Cohesion: 0.48
Nodes (5): AllArgsConstructor, Getter, NoArgsConstructor, Setter, JobApplicationResponse

## Ambiguous Edges - Review These
- `Development Rules` → `DOCUMENT_API.md — ATS REST API Documentation`  [AMBIGUOUS]
  CLAUDE.md · relation: references

## Knowledge Gaps
- **68 isolated node(s):** `com.example:ats`, `APPLICATION_CREATED`, `SCREENING`, `INTERVIEW`, `OFFER` (+63 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **4 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **What is the exact relationship between `Development Rules` and `DOCUMENT_API.md — ATS REST API Documentation`?**
  _Edge tagged AMBIGUOUS (relation: references) - confidence is low._
- **Why does `ApiResponse` connect `Admin Company Controller` to `InterviewFeedbackResponse`, `Admin Job Controller`, `Candidate Controller`, `Recruiter Controller`, `Resume Controller`, `Global Exception Handling & Config`, `RecruiterResponse`, `AdminInterviewController.java`, `Admin Application Controller`, `Auth Controller`, `Candidate Application Controller`, `RecruiterInterviewController.java`, `Admin User Controller`, `Recruiter Application Controller`, `Admin User Management`, `InterviewController.java`, `API Response Serialization Tests`?**
  _High betweenness centrality (0.214) - this node is a cross-community bridge._
- **Why does `ResourceNotFoundException` connect `ResourceNotFoundException` to `Admin Job Controller`, `Company & Job Mapping/Repos`, `InterviewFeedbackEntity`, `JobService.java`, `Company`, `Admin Company Controller`, `Recruiter Controller`, `Notification`, `User Entity & Repositories`, `Global Exception Handling & Config`, `Job Application Mapping/Repos`, `JWT Service`, `Job Application Service`, `CandidateEntity`, `Auth Service & User Mapper`?**
  _High betweenness centrality (0.134) - this node is a cross-community bridge._
- **Why does `JobResponse` connect `Admin Job Controller` to `Company & Job Mapping/Repos`, `Admin Company Controller`, `User Request/Response DTOs`, `JobApplicationResponse`?**
  _High betweenness centrality (0.093) - this node is a cross-community bridge._
- **What connects `com.example:ats`, `APPLICATION_CREATED`, `SCREENING` to the rest of the system?**
  _68 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Admin Job Controller` be split into smaller, more focused modules?**
  _Cohesion score 0.05524634096062667 - nodes in this community are weakly interconnected._
- **Should `Company & Job Mapping/Repos` be split into smaller, more focused modules?**
  _Cohesion score 0.05428150641699979 - nodes in this community are weakly interconnected._