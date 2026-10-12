@echo off
echo ========================================================
echo   HEALTHCONNECT - Starting Backend and Frontend Services
echo ========================================================

echo.
echo Starting Backend Service (Spring Boot on http://localhost:8080)...
start "HEALTHCONNECT Backend" cmd /k "cd backend && mvnw.cmd spring-boot:run"

echo.
echo Starting Frontend Service (React Vite on http://localhost:3000)...
start "HEALTHCONNECT Frontend" cmd /k "cd frontend && npm.cmd run dev"

echo.
echo ========================================================
echo   HEALTHCONNECT Services are launching!
echo   - Frontend: http://localhost:3000
echo   - Backend API / Health: http://localhost:8080/actuator/health
echo   - Swagger API Docs: http://localhost:8080/swagger-ui.html
echo   - H2 Database Console: http://localhost:8080/h2-console
echo ========================================================
