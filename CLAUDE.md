# ATS project

## Technology Stack

| Technology | Version | Purpose |
|---|---:|---|
| Java | 17 | Programming language and runtime |
| Maven Wrapper | 3.9.16 | Build and dependency management |
| Spring Boot | 3.5.7 | Application framework |
| Spring Web / MVC | 6.2.12 | REST APIs and public file serving |
| Spring Data JPA | 3.5.5 | Persistence abstraction |
| Hibernate ORM | 6.6.33.Final | JPA implementation |
| Jakarta Persistence API | 3.1.0 | Persistence API and annotations |
| Jakarta Validation API | 3.0.2 | Request validation |
| Spring Security | 6.5.6 | Security filters and password encoding |
| Spring WebSocket | 6.2.12 | Real-time communication dependency |
| OAuth2 Resource Server | 6.5.6 | JWT and OAuth2 resource-server support |
| MySQL Server | 8.4 | Relational database |
| MySQL Connector/J | 9.4.0 | JDBC driver |
| Lombok | 1.18.42 | Boilerplate code generation |
| JUnit Jupiter | 5.12.2 | Testing framework |
| Mockito | 5.17.0 | Mocking framework |
| MapStruct | 1.6.3 | dependency |

### Package structure

| Package | Responsibility |
|---|---|
| `adapter.controller` | REST API endpoints and inbound HTTP handling |
| `adapter.handler` | Global API exception handling |
| `application.dto.request`, `application.dto.response` | Request and response DTOs used by application use cases and controllers |
| `application.mapper` | MapStruct mappers between domain models, DTOs, and persistence models |
| `application.port.in` | Use-case interfaces called by inbound adapters |
| `application.service` | Business logic, transactions, and orchestration |
| `application.port.out` | Data-access interfaces required by the application |
| `domain.model` | Framework-independent business models and enums |
| `domain.exception` | Business and domain exceptions |
| `domain.result` | Domain-level result objects returned by business workflows |
| `infrastructure.persistence.adapter` | Output-port implementations and data mapping |
| `infrastructure.persistence.entity` | JPA table and relationship mappings |
| `infrastructure.persistence.repository` | Spring Data JPA repositories |
| `infrastructure.config` | Security, file upload, and static-resource configuration |
## Architecture

The project follows Clean/Hexagonal Architecture:

```text
HTTP request
    |
    v
adapter/controller
    |  REST endpoints, request/response DTOs, validation, exception handling
    v
application/port/in
    |  Inbound use-case contracts
    v
application/service
    |  Business workflow orchestration and transaction management
    v
domain
    |  Business models, enums, and domain exceptions
    v
application/port/out
    |  Persistence contracts independent of Spring Data
    v
infrastructure/persistence/adapter
    |  Mapping between domain models and JPA entities
    v
infrastructure/persistence/repository
    |  Spring Data JPA repositories
    v
MySQL
```

### Rules
- Do not modify the model in the domain.
- DTO user MapStruct
- After completing an assigned task, summarize the changes made and update `API_DOCUMENT.md` and `GROUP_API.md` when the task changes API behavior, request/response fields, validation, security, or business workflow.
The following testing rules apply to this project:

- Do not generate or add automated test code.
- Running the test suite is not required.
- Development tasks should be completed without creating or executing tests.

### Commands
- mvn clean compile
