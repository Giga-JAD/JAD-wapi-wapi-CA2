# Use Maven with JDK 17 to build the project
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Use OpenJDK instead of Tomcat
FROM openjdk:17.0.1-jdk-slim
WORKDIR /app
COPY --from=build /app/target/app.jar app.jar

# Expose the correct port
#ENV address=0.0.0.0
#ENV PORT=8081
EXPOSE 8081

# Run the Spring Boot JAR directly
ENTRYPOINT ["java","-jar","app.jar"]