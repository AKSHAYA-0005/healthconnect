# HEALTHCONNECT – AI-Powered Patient Triage & Chatbot System

An intelligent, rule-based healthcare triage assistant built using Java 17, Spring Boot 3.x, MySQL 8, React 18 (Vite), and Docker Compose.

---

## ⚠️ Mandatory Healthcare Safety Disclaimer

> **"This result is a preliminary automated triage recommendation and is not a medical diagnosis. If symptoms are severe, worsening, or concerning, seek professional medical care promptly."**

### Emergency Handling:
- Red-flag symptoms (chest pain, shortness of breath, sudden numbness, loss of consciousness, severe bleeding) immediately trigger **HIGH** urgency and display a prominent red emergency banner:
  **"Possible emergency. Contact your local emergency number (112) or go to the nearest emergency department now."**

---

## 🛠️ Technology Stack

- **Backend:** Java 17, Spring Boot 3.2.3, Spring Data JPA, Spring Security 6, JWT, Jakarta Validation, springdoc-openapi (Swagger UI).
- **Frontend:** React 18, Vite, React Router v6, Axios, Custom Glassmorphism CSS System.
- **Database:** MySQL 8.0.
- **DevOps:** Docker, Docker Compose, Nginx Proxy, Non-root containers, Healthchecks.

---

## 🚀 Quick Start with Docker Compose

Run the entire system in one command:

```bash
docker compose up --build
```

### Access Ports:
- **Frontend Web Application:** [http://localhost:3000](http://localhost:3000)
- **Backend REST API / Actuator:** [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)
- **Swagger API Documentation:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **MySQL Database:** `localhost:3306`

---

## 🔑 Pre-seeded Accounts for Testing

| Role | Email | Password |
|---|---|---|
| **Administrator** | `admin@healthconnect.com` | `admin123` |
| **Doctor (Cardiology)** | `doctor.jenkins@healthconnect.com` | `doc123` |
| **Doctor (Neurology)** | `doctor.chang@healthconnect.com` | `doc123` |
| **Doctor (General Med)** | `doctor.rivera@healthconnect.com` | `doc123` |
| **Patient** | Register a new account on [http://localhost:3000/register](http://localhost:3000/register) | Custom |

---

## 🧪 Running Unit & Integration Tests

```bash
cd backend
mvn test
```
