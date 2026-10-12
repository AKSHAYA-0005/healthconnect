# 🌐 HEALTHCONNECT - Deployment & Hosting Guide

This guide provides step-by-step instructions to deploy your HEALTHCONNECT system for free online or with Docker.

---

## 🚀 Deployment Options Overview

| Option | Frontend Hosting | Backend Hosting | Database | Cost | Difficulty |
|---|---|---|---|---|---|
| **Option 1 (Recommended)** | [Vercel](https://vercel.com) | [Render](https://render.com) | H2 (in-memory) or [Aiven MySQL](https://aiven.io) | **Free** | Easy (10 mins) |
| **Option 2 (All-in-One)** | [Render](https://render.com) | [Render](https://render.com) | H2 or Render Postgres/MySQL | **Free** | Very Easy (Blueprint) |
| **Option 3 (Containerized)** | Docker Hub | [Railway](https://railway.app) / [Fly.io](https://fly.io) | MySQL 8.0 | Free tier / Pay-per-use | Intermediate |
| **Option 4 (Self-Hosted)** | Local / VPS | Docker Compose | MySQL 8.0 Container | Self-hosted | Beginner |

---

## 🥇 Option 1: Deploy Frontend on Vercel + Backend on Render (Recommended)

### Step 1: Deploy Backend to Render

1. Sign up / Log in to [Render](https://render.com).
2. Click **New +** > **Web Service**.
3. Connect your GitHub / GitLab repository containing this project.
4. Fill in the following settings:
   - **Name:** `healthconnect-backend`
   - **Root Directory:** `backend`
   - **Runtime:** `Docker` (or Java with Maven)
   - **Dockerfile Path:** `./Dockerfile`
   - **Instance Type:** `Free`
5. Under **Environment Variables**, add:
   - `PORT`: `8080`
   - `APP_EMERGENCY_NUMBER`: `112`
6. Click **Create Web Service**.
7. Once deployed, Render will provide a public URL (e.g., `https://healthconnect-backend.onrender.com`).
   - Test it at: `https://healthconnect-backend.onrender.com/actuator/health`

---

### Step 2: Deploy Frontend to Vercel

1. Sign up / Log in to [Vercel](https://vercel.com).
2. Click **Add New...** > **Project**.
3. Select your GitHub repository.
4. In the project setup:
   - **Framework Preset:** `Vite`
   - **Root Directory:** Click `Edit` and select `frontend`
   - **Build Command:** `npm run build`
   - **Output Directory:** `dist`
5. Under **Environment Variables**, add:
   - `VITE_API_BASE_URL`: `https://healthconnect-backend.onrender.com/api` *(Replace with your Render backend URL)*
6. Click **Deploy**.
7. Vercel will give you a live production URL (e.g., `https://healthconnect.vercel.app`)!

---

## 🥈 Option 2: All-in-One 1-Click Deploy via Render Blueprint

This repository includes a [`render.yaml`](./render.yaml) blueprint file.

1. Push your repository to GitHub.
2. Go to [Render Blueprints](https://dashboard.render.com/blueprints).
3. Click **New Blueprint Instance**.
4. Connect your repository.
5. Render will automatically configure and launch:
   - `healthconnect-backend` (Docker container on port 8080)
   - `healthconnect-frontend` (Static React SPA linked to the backend)
6. Click **Apply**!

---

## 🥉 Option 3: Deploy with Railway

1. Sign up at [Railway.app](https://railway.app).
2. Click **New Project** > **Deploy from GitHub repo**.
3. Railway will detect the Dockerfiles and prompt to deploy services.
4. Add a free **MySQL** database plugin from the Railway dashboard if you want persistent MySQL storage.
5. Set environment variables on the backend service:
   - `DB_URL`: `${{MySQL.MYSQL_URL}}`
   - `DB_USER`: `${{MySQL.MYSQLUSER}}`
   - `DB_PASSWORD`: `${{MySQL.MYSQLPASSWORD}}`

---

## 💻 Local Running & Development Links

When running locally on your computer:

| Service | Local URL | Description |
|---|---|---|
| **Frontend Web App** | [http://localhost:3000](http://localhost:3000) | Patient & Doctor Portal |
| **Backend REST API** | [http://localhost:8080/](http://localhost:8080/) | Spring Boot Root Endpoint |
| **Swagger UI Docs** | [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) | Interactive API Explorer |
| **Spring Boot Actuator** | [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health) | System Health Check |
| **H2 Database Console** | [http://localhost:8080/h2-console](http://localhost:8080/h2-console) | In-Memory Database GUI (JDBC URL: `jdbc:h2:mem:healthconnect_db`) |

### Default Login Credentials for Testing

| Role | Email | Password |
|---|---|---|
| **Administrator** | `admin@healthconnect.com` | `admin123` |
| **Doctor (Cardiology)** | `doctor.jenkins@healthconnect.com` | `doc123` |
| **Doctor (Neurology)** | `doctor.chang@healthconnect.com` | `doc123` |
| **Doctor (General Med)** | `doctor.rivera@healthconnect.com` | `doc123` |
| **New Patient** | Register at [http://localhost:3000/register](http://localhost:3000/register) | Any password |

### How to Run Locally Anytime:
Double-click `run-local.bat` in the project root or run:
```cmd
run-local.bat
```
