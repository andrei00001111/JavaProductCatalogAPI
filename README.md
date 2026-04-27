# E-commerce Product Catalog Microservice

Spring Boot microservice that models an e-commerce catalog domain with product CRUD, optimized search/filtering, pagination, sorting, seller/category management, and deployment-ready infrastructure.

## Tech Stack
- Java 21
- Spring Boot 3.3
- Spring Data JPA
- PostgreSQL + Flyway
- OpenAPI/Swagger
- Docker + Docker Compose
- JUnit 5 + Spring Boot Test

## Features
- Product CRUD with category/seller references
- Advanced product search:
  - Text query (`name`, `description`, `sku`)
  - `categoryId`, `sellerId`
  - `minPrice`, `maxPrice`
  - `inStock`, `active`
- Pagination and sorting via Spring `Pageable`
- Category and seller management endpoints
- Global API error handling and validation responses
- Search-oriented indexes in migration scripts

## API Base
- Base URL: `http://localhost:8080/api/v1`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`

## Run Locally (without Docker)
1. Start PostgreSQL (or use Docker Compose only for DB).
2. Ensure DB credentials match `src/main/resources/application.yml` env defaults.
3. Run:

```bash
mvn spring-boot:run
```

## Run with Docker Compose

```bash
docker compose up --build
```

Services:
- App: `http://localhost:8080`
- PostgreSQL: `localhost:5432`

## Build and Test

```bash
mvn clean test
```

## Project Structure
See `project.md` for the implementation plan, checklist, and architecture rationale.
