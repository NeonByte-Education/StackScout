# Build stage
FROM gradle:8-jdk21-alpine AS build
WORKDIR /app
COPY backend/ ./backend/
COPY settings.gradle.kts ./
COPY gradle/ ./gradle/
COPY gradlew* ./
RUN cd backend && chmod +x ../gradlew && ../gradlew build -x test --no-daemon

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/backend/build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]