# UserHub — Your First Full-Stack Web App

UserHub is a simple user management application built with **Spring Boot** (REST API), **ReactJS** (Vite) and an **H2** database. It demonstrates user registration, login with password hashing, token-based authentication, role-based authorization (`USER` / `ADMIN`), a protected dashboard, an admin-only users page, server-side logout, and profile editing.

## Technologies Used

| Layer | Technology |
|-------|------------|
| Backend | Java 17+, Spring Boot 3.3, Spring Web, Spring Data JPA, Bean Validation, BCrypt (`spring-security-crypto`) |
| Database | H2 (file-based) |
| Frontend | ReactJS 18, Vite 5, React Router 6, Axios |
| Build tools | Maven, npm |

## System Requirements / Prerequisites

- **JDK 17 or newer** (`java -version`)
- **Maven 3.8+** (`mvn -v`)
- **Node.js 18+** and **npm** (`node -v`, `npm -v`)
- **Git**
- Free ports **8080** (backend) and **5173** (frontend)

## Project Structure

```
Lastname-Firstname-First-Web-App/
├── backend/     Spring Boot source (entity, repository, service, controller, dto, security, exception, config)
├── frontend/    ReactJS (Vite) source
├── docs/        REQUIREMENTS.md and screenshots/
└── README.md
```

## Setup and Running

### 1. Clone

```bash
git clone https://github.com/<your-username>/Lastname-Firstname-First-Web-App.git
cd Lastname-Firstname-First-Web-App
```

### 2. Backend (Spring Boot)

```bash
cd backend
mvn spring-boot:run
```

The API starts on **http://localhost:8080**. On first start it creates the H2 database in `backend/data/` and seeds the default admin account.

To build a jar instead: `mvn clean package` then `java -jar target/userhub-backend-1.0.0.jar`.

### 3. Frontend (React + Vite)

Open a **second terminal**:

```bash
cd frontend
npm install
npm run dev
```

The app starts on **http://localhost:5173**.

## Application URLs and Ports

| Service | URL |
|---------|-----|
| React frontend | http://localhost:5173 |
| Spring Boot API | http://localhost:8080/api |
| H2 console | http://localhost:8080/h2-console |

H2 console login: JDBC URL `jdbc:h2:file:./data/userhubdb` (run the backend from the `backend/` folder), user `sa`, empty password.

## Default Admin Account (seeded on startup)

| Username | Password |
|----------|----------|
| `admin` | `admin123` |

Override with environment variables `USERHUB_ADMIN_USERNAME` and `USERHUB_ADMIN_PASSWORD` before starting the backend. Change the default password for anything beyond local development.

## How to Test the Application

1. Open http://localhost:5173 and click **Register**. Create a normal user (the password must be at least 6 characters). Try invalid input to see validation messages, and a duplicate username to see the `409` conflict message.
2. **Log in** with that user. You land on the protected **Dashboard**.
3. Open **My Profile**, change your full name, and save. The dashboard shows the updated name.
4. Confirm the **Users** link is **not** shown, and that visiting `/admin/users` redirects you back to the dashboard.
5. Click **Logout**. The token is deleted on the server. Reusing it returns `401` (see the curl example below).
6. Log in as `admin` / `admin123`. The **Users** link appears; open it to see all registered users.

### API testing with curl

```bash
# Register
curl -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" \
  -d '{"username":"juan","email":"juan@example.com","fullName":"Juan Dela Cruz","password":"secret123"}'

# Login (copy the token from the response)
curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" \
  -d '{"username":"juan","password":"secret123"}'

# Protected endpoint
curl http://localhost:8080/api/users/me -H "Authorization: Bearer <TOKEN>"

# Admin-only endpoint (403 for USER, 200 for ADMIN)
curl http://localhost:8080/api/admin/users -H "Authorization: Bearer <TOKEN>"

# Logout, then retry /api/users/me with the same token -> 401
curl -X POST http://localhost:8080/api/auth/logout -H "Authorization: Bearer <TOKEN>"
```

## API Summary

| Method | Endpoint | Access |
|--------|----------|--------|
| POST | `/api/auth/register` | Public |
| POST | `/api/auth/login` | Public |
| POST | `/api/auth/logout` | Authenticated |
| GET | `/api/users/me` | Authenticated |
| GET | `/api/users/dashboard` | Authenticated |
| PUT | `/api/users/me` | Authenticated |
| GET | `/api/admin/users` | ADMIN only |

See [docs/REQUIREMENTS.md](docs/REQUIREMENTS.md) for full feature details and [docs/screenshots/](docs/screenshots/) for screenshots.

## Additional Configuration

- **CORS:** the backend allows the origin set in `backend/src/main/resources/application.properties` (`userhub.cors.allowed-origin`, default `http://localhost:5173`).
- **API URL for the frontend:** copy `frontend/.env.example` to `frontend/.env` and edit `VITE_API_URL` if the backend runs elsewhere.
- **Fresh database each run:** change `spring.datasource.url` to `jdbc:h2:mem:userhubdb`, or delete `backend/data/`.
- **Token lifetime:** 8 hours (`TOKEN_HOURS` in `AuthService`).

## Notes

- `node_modules/`, `target/`, `dist/` and the `data/` database folder are git-ignored.
- Passwords are stored as BCrypt hashes and are never returned by the API.
