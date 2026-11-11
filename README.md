# Pathway — Placement Management System

A full-stack placement portal with student and administrator workspaces. The API uses Java 21, Spring Boot, Spring Security, JPA and Flyway with MySQL. The client uses React, Vite and Axios. Student accounts, profiles, job postings, settings and applications are stored in MySQL. Resume PDFs are held in private local storage and are served only through authenticated endpoints.

## Project layout

```text
backend/                 Spring Boot API and Flyway migrations
frontend/                React + Vite client
docker-compose.yml       Local MySQL service
```

## Prerequisites

- Java 21 (the current project target; Java 17 is not sufficient)
- Maven 3.9+
- Node.js 20+ and npm
- Docker Desktop, or a local MySQL 8 server

## Local startup

1. Copy `.env.example` to `.env` and change the development secrets/passwords.
2. Start the database with `docker compose up -d mysql`.
3. Start the API from `backend`: `mvn spring-boot:run`.
4. Start the client from `frontend`: `npm install` then `npm run dev`.
5. Open <http://localhost:5173>.

Flyway creates the schema on first API startup. Hibernate validates it; it does not modify the schema. The API defaults to `http://localhost:8080` and the client can override it with `VITE_API_URL`.

Create the initial administrator with `ADMIN_EMAIL` and `ADMIN_PASSWORD` in the environment before starting the API. The app only creates that account if the email is not already registered. No administrator credentials are committed in this repository. Student accounts are created through the registration page.

## Environment variables

| Variable | Default | Purpose |
|---|---|---|
| `DB_URL` | local MySQL URL | JDBC connection URL |
| `DB_USERNAME` / `DB_PASSWORD` | `placement` / `placement` | Database credentials |
| `JWT_SECRET` | development-only value | HMAC signing key; replace with a long random secret |
| `JWT_EXPIRATION_MS` | `86400000` | Token lifetime |
| `STORAGE_PATH` | `./uploads` | Private resume directory |
| `CORS_ORIGIN` | `http://localhost:5173` | Allowed browser origin |
| `SERVER_PORT` | `8080` | API port |
| `ADMIN_EMAIL` / `ADMIN_PASSWORD` | blank | Optional initial admin account |
| `VITE_API_URL` | `http://localhost:8080/api` | Frontend API base URL |

Uploaded PDFs are not in the public web root. Back up the storage directory along with the database. For deployment, implement the same storage boundary using a private S3 bucket and short-lived signed downloads.

## API outline

All routes are under `/api`. Bearer JWT authentication is required except where noted.

| Area | Routes |
|---|---|
| Auth | `POST /auth/register`, `POST /auth/login`, `GET /auth/me` |
| Public jobs | `GET /jobs` (query filters + pagination), `GET /jobs/{id}` |
| Student | `GET/PUT /profile/me`, `POST/GET/DELETE /resume`, `GET /dashboard/me` |
| Applications | `POST /applications/jobs/{jobId}`, `GET /applications/me`, `GET /applications/me/{id}`, `PATCH /applications/{id}/withdraw` |
| Settings | `GET/PUT /settings`, `PATCH /settings/password` |
| Notifications | `GET /notifications`, `PATCH /notifications/{id}/read` |
| Admin dashboard | `GET /admin/dashboard` |
| Admin jobs | `GET /admin/jobs/all`, `POST /admin/jobs`, `PUT/DELETE /admin/jobs/{id}`, `PATCH /admin/jobs/{id}/publish`, `PATCH /admin/jobs/{id}/close` |
| Admin students | `GET /admin/students`, `GET /admin/students/{id}`, `GET /admin/students/{id}/resume` |
| Admin applications | `GET /admin/applications`, `GET /admin/jobs/{jobId}/applications`, `PATCH /admin/applications/{id}/status` |
| Public metrics | `GET /public/stats` |

Applications are unique per student/job in the database. The API enforces published state, deadline, resume presence, CGPA and department eligibility. Admin routes require the `ADMIN` role; student application/profile ownership is checked server side.

## Verification

Run frontend production build with `cd frontend && npm run build`. Run backend tests with `cd backend && mvn test`. Current environment notes: Java 17 is installed in the workspace environment, but this project targets Java 21. No database server is assumed to be running; integration workflows require the local MySQL service.

## AWS preparation

Configuration is environment-driven and file storage is isolated behind API endpoints. For AWS, use a private S3 bucket through a storage adapter, RDS MySQL, Secrets Manager for JWT/database secrets, and a TLS-enabled API origin. Do not expose uploaded resumes through public bucket URLs. No AWS resources are provisioned by this project.
