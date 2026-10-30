# UserHub — Requirements and Implemented Features

Status key: **Implemented** = code written for the feature. Update this file with your own test results and screenshots after running the app.

## A. Backend (Spring Boot)

| # | Feature | Description | Expected behavior | Endpoint | Status |
|---|---------|-------------|-------------------|----------|--------|
| B1 | REST API | Spring Boot REST API under `/api` | JSON requests/responses | `/api/**` | Implemented |
| B2 | Layered architecture | Entity, Repository, Service, Controller, DTO packages | Controllers call services; services call repositories; DTOs shape I/O | — | Implemented |
| B3 | H2 + Spring Data JPA | File-based H2 database with JPA entities `User` and `AuthToken` | Data persists across restarts; H2 console enabled | `/h2-console` | Implemented |
| B4 | User registration | Create a new account with validation | `201 Created` with the user (no password); `400` with field errors for invalid input; `409` for duplicate username/email. New users always get role `USER` | `POST /api/auth/register` | Implemented |
| B5 | Authentication and password hashing | Login with username and password; passwords stored as BCrypt hashes | `200` with token and user; `401` for bad credentials | `POST /api/auth/login` | Implemented |
| B6 | Token-based authentication | Random token stored in DB with 8h expiry, sent as `Authorization: Bearer <token>` | Missing, invalid or expired token returns `401` | all protected endpoints | Implemented |
| B7 | Role-based authorization | `USER` and `ADMIN` roles enforced by `@RequiresAuth(role = ...)` | Wrong role returns `403` | `/api/admin/**` | Implemented |
| B8 | Protected dashboard endpoints | Authenticated-only endpoints for the dashboard | Return current user's data and welcome message | `GET /api/users/me`, `GET /api/users/dashboard` | Implemented |
| B9 | Exception handling | `@RestControllerAdvice` returning a consistent error body | Correct HTTP codes (400, 401, 403, 404, 409, 500) with `status`, `error`, `message`, `fieldErrors` | all | Implemented |

## B. Frontend (ReactJS)

| # | Feature | Description | Expected behavior | Status |
|---|---------|-------------|-------------------|--------|
| F1 | Vite React app | React 18 project built with Vite | `npm run dev` serves on port 5173 | Implemented |
| F2 | Registration page (`/register`) | Form for full name, username, email, password | Shows server validation errors; redirects to login on success | Implemented |
| F3 | Login page (`/login`) | Username/password form | Stores token; redirects to dashboard; shows error on failure | Implemented |
| F4 | Protected dashboard (`/dashboard`) | Shows the logged-in user's details | Fetches data from the API using the token | Implemented |
| F5 | React Router | Client-side navigation | Routes: `/login`, `/register`, `/dashboard`, `/profile`, `/admin/users` | Implemented |
| F6 | Axios API layer | Shared Axios instance with base URL | Talks to the Spring Boot API | Implemented |
| F7 | Token handling | Token saved in `localStorage`, attached by an Axios request interceptor | Cleared on logout or on a `401` response | Implemented |
| F8 | Protected routes and logout | `ProtectedRoute` guards pages; navbar Logout button | Unauthenticated users go to `/login`; non-admins cannot open admin page | Implemented |

## C. Additional Features (50 points)

| # | Feature | Description | Expected behavior | Endpoint | Status |
|---|---------|-------------|-------------------|----------|--------|
| C1 | Seed an ADMIN user (10 pts) | `CommandLineRunner` (`DataSeeder`) creates a default admin on startup if missing | Login with `admin` / `admin123` returns role `ADMIN`; not duplicated on restart | — | Implemented |
| C2 | Admin Users page (15 pts) | ADMIN-only page listing all registered users | Table of id, name, username, email, role, created date; USER gets `403` and is redirected | `GET /api/admin/users`, UI `/admin/users` | Implemented |
| C3 | Server-side logout (10 pts) | Endpoint deletes the token from the database | Token returns `401` on any later request; frontend calls it on Logout | `POST /api/auth/logout` | Implemented |
| C4 | Edit My Profile (15 pts) | Authenticated users update their own full name | `200` with updated user; blank name returns `400`; UI at `/profile` | `PUT /api/users/me` | Implemented |

## Error Response Format

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "fieldErrors": { "email": "Email must be valid" },
  "timestamp": "2026-10-30T15:00:00"
}
```

## Screenshots

Save screenshots in `docs/screenshots/` after running the app, for example:

- `register.png` — registration page
- `login.png` — login page
- `dashboard.png` — user dashboard
- `admin-users.png` — admin users page
- `edit-profile.png` — edit profile
