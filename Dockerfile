# Use Maven with JDK 17 to build the project
FROM maven:3.8.7-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Use OpenJDK instead of Tomcat
FROM openjdk:17
WORKDIR /app

# Copy .env file only if it exists (optional)
COPY .env /app/.env

# Copy the built JAR file
COPY --from=build /app/target/app.jar app.jar

# Expose the correct port
ENV PORT=8081
EXPOSE 8081

# Run the Spring Boot JAR directly, ensuring environment variables are loaded
CMD ["sh", "-c", "java -jar app.jar --server.port=${PORT} --spring.config.location=file:/app/.env"]
