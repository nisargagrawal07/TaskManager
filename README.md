# Task Management REST API

A complete learning project using **Java 21, Spring Boot 3.5.6, Hibernate / Spring Data JPA, PostgreSQL, Flyway, Spring Security, JWT, Jakarta Validation, Swagger, Maven and JUnit**.

## Run entirely online with GitHub Codespaces

1. Create a **new GitHub repository** and upload the *contents* of this folder (including `.devcontainer`). Commit your changes.
2. On GitHub select **Code → Codespaces → Create codespace on main**. The dev container installs Java 21, Maven and Docker-in-Docker.
3. In the Codespaces terminal run:

```bash
docker compose up -d
export JWT_SECRET="$(openssl rand -hex 32)"
mvn spring-boot:run
```

4. In the **Ports** tab open forwarded port **8080**. Swagger UI is at `/swagger-ui/index.html` and health is at `/actuator/health`. Use the forwarded URL as your API base URL.

**Important:** Each new terminal needs the JWT_SECRET environment variable; keep the service running in the same terminal where you export it. Codespaces may make forwarded ports public if you change visibility; keep yours private. Do not commit secrets.

## Run locally

Install JDK 21, Maven 3.9+ and Docker. Run the same three commands above from the project root.

## API examples

```bash
# Register (returns JWT)
curl -s -X POST http://localhost:8080/api/auth/register \
 -H 'Content-Type: application/json' \
 -d '{"name":"Nisarg","email":"nisarg@example.com","password":"StrongPass123!"}'

# Login (returns JWT)
curl -s -X POST http://localhost:8080/api/auth/login \
 -H 'Content-Type: application/json' \
 -d '{"email":"nisarg@example.com","password":"StrongPass123!"}'

# Paste the token returned above:
export TOKEN='PASTE_JWT_HERE'

# Create task
curl -X POST http://localhost:8080/api/tasks \
 -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' \
 -d '{"title":"Learn Spring Boot","description":"Build a REST API","status":"TODO","priority":"HIGH","dueDate":"2026-10-01"}'

# List and filter (pagination starts at page=0)
curl -H "Authorization: Bearer $TOKEN" 'http://localhost:8080/api/tasks?status=TODO&page=0&size=10&sort=createdAt,desc'

# Get / update / delete task 1
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/tasks/1
curl -X PUT -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' \
 -d '{"title":"Learn JPA","description":"Mappings","status":"IN_PROGRESS","priority":"HIGH"}' http://localhost:8080/api/tasks/1
curl -X DELETE -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/tasks/1
```

In Codespaces, substitute the **forwarded 8080 URL** for `http://localhost:8080` when calling from your own computer. From the Codespaces terminal, localhost works.

### Routes

| Method | Path | Authentication |
|---|---|---|
| POST | /api/auth/register | Public |
| POST | /api/auth/login | Public |
| POST | /api/tasks | Bearer JWT |
| GET | /api/tasks | Bearer JWT; optional `status`, `page`, `size`, `sort` |
| GET | /api/tasks/{id} | Bearer JWT; owner only |
| PUT | /api/tasks/{id} | Bearer JWT; owner only |
| DELETE | /api/tasks/{id} | Bearer JWT; owner only |
| GET | /actuator/health | Public |

`status`: `TODO`, `IN_PROGRESS`, `DONE`; `priority`: `LOW`, `MEDIUM`, `HIGH`. POST and PUT require `title`, `status` and `priority`. PUT replaces all editable fields.

## Architecture

```
HTTP → Controller → Service → Spring Data JPA Repository → Hibernate → PostgreSQL
          ↑           ↑
    Bean validation  Ownership checks
          ↑
    Spring Security JWT filter
```

- `model/`: JPA entities, lazy many-to-one association, lifecycle timestamps, enums.
- `dto/`: Java records and Bean Validation.
- `controller/`: HTTP mapping and centralized validation errors.
- `service/`: business logic, transactions and per-user access control.
- `repository/`: derived Spring Data JPA queries and pagination.
- `security/`: BCrypt password hashing, stateless JWT auth.
- `db/migration/`: Flyway versioned SQL schema and indexes.
- `.devcontainer/`: one-click Codespaces environment.

## Tests

```bash
mvn test
```

The tests use an in-memory H2 database in PostgreSQL compatibility mode, exercising registration, task creation/listing, owner isolation, authentication and validation. H2 is convenient for fast tests but **not** a substitute for a PostgreSQL integration test before production deployment.

## Learning exercises

1. Add PATCH `/api/tasks/{id}/status` and compare PUT vs PATCH.
2. Add project entities and a `@OneToMany` relationship; investigate N+1 queries.
3. Implement optimistic locking using `@Version`.
4. Add search, sorting, a PostgreSQL Testcontainers suite and refresh tokens.
5. Add role-based authorization and rate limiting.

## Security and deployment notes

This is a learning service, **not a production-hardened identity system**. For public deployment, use managed secrets, HTTPS, secure CORS rules, rate limiting, refresh-token rotation or an external identity provider, monitoring and PostgreSQL integration tests. The compose password is development-only. JWTs expire after one hour. Passwords are BCrypt-hashed. Task lookups always include the authenticated owner's ID.
