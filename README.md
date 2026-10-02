# Employee Management API

CRUD REST API built with Spring Boot for the internSpark Java Developer internship
(covers Tasks 1-4: REST API, DB migrations, unit testing & logging, Docker deploy).

## Tech stack
- Java 17, Spring Boot 3.3
- Spring Web, Spring Data JPA, Bean Validation
- PostgreSQL (docker profile) / H2 in-memory (dev profile)
- Flyway for schema migrations
- JUnit 5 + Mockito + AssertJ for tests
- SLF4J + Logback for logging
- Docker & docker-compose

## Run locally (no Docker, H2 in-memory DB)
```bash
mvn spring-boot:run
```
App starts on http://localhost:8080 using the `dev` profile (H2). Console at `/h2-console`.

## Run with Docker (Spring Boot app + PostgreSQL)
```bash
docker-compose up --build
```
This starts:
- `db` — PostgreSQL 16 on port 5432
- `app` — the API on port 8080, using the `docker` profile

Environment variables used by the `docker` profile (set in `docker-compose.yml`):
| Variable | Default | Purpose |
|---|---|---|
| `DB_HOST` | db | Postgres host |
| `DB_PORT` | 5432 | Postgres port |
| `DB_NAME` | employeedb | Database name |
| `DB_USER` | employee_user | DB username |
| `DB_PASSWORD` | employee_pass | DB password |
| `SERVER_PORT` | 8080 | App port |

To stop: `docker-compose down` (add `-v` to also drop the DB volume).

## Run tests
```bash
mvn test
```
Surefire reports are generated under `target/surefire-reports`.

## API endpoints
| Method | Path | Description |
|---|---|---|
| POST | /api/v1/employees | Create an employee (201 Created + Location header) |
| GET | /api/v1/employees | List all employees (200 OK) |
| GET | /api/v1/employees/{id} | Get one employee (200 OK / 404 Not Found) |
| PUT | /api/v1/employees/{id} | Update an employee (200 OK / 404 Not Found) |
| DELETE | /api/v1/employees/{id} | Delete an employee (204 No Content / 404 Not Found) |

## Sample curl commands
```bash
# Create
curl -X POST http://localhost:8080/api/v1/employees \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Asha","lastName":"Rao","email":"asha.rao@example.com","department":"Engineering","salary":65000,"dateOfJoining":"2023-06-12"}'

# List all
curl http://localhost:8080/api/v1/employees

# Get one
curl http://localhost:8080/api/v1/employees/1

# Update
curl -X PUT http://localhost:8080/api/v1/employees/1 \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Asha","lastName":"Rao-Kulkarni","email":"asha.rao@example.com","department":"Product","salary":70000,"dateOfJoining":"2023-06-12"}'

# Delete
curl -X DELETE http://localhost:8080/api/v1/employees/1
```
A ready-made Postman collection is in `postman_collection.json` — import it into Postman.

## Database migrations (Flyway)
Located in `src/main/resources/db/migration`:
- `V1__create_employee_table.sql` — creates the `employees` table
- `V2__seed_employee_data.sql` — seeds 5 sample employees

Flyway runs automatically on application startup against whichever datasource is active.

## Logging
Configured in `src/main/resources/logback-spring.xml`:
- Console output (human-readable pattern)
- Rolling file output to `logs/employee-api.log` (daily rollover, 10MB max size, 7-day history)
- `com.internspark.employeeapi` and Hibernate SQL logged at DEBUG for visibility

## Project structure
```
src/main/java/com/internspark/employeeapi/
  controller/   REST controllers
  service/      Service interfaces + impl
  repository/   Spring Data JPA repositories
  entity/       JPA entities
  dto/          Request/response DTOs + error shape
  exception/    Custom exceptions + @RestControllerAdvice handler
src/main/resources/
  application.yml       dev / docker profiles
  db/migration/         Flyway SQL scripts
  logback-spring.xml    logging config
src/test/java/...       JUnit + Mockito unit tests
```
