# Stage 1: Build JAR with Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime Environment with Non-Root User
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN apk add --no-cache curl && addgroup -S spring && adduser -S spring -G spring
COPY --from=build /app/target/healthconnect-backend-1.0.0.jar app.jar
RUN chown -R spring:spring /app
USER spring
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
