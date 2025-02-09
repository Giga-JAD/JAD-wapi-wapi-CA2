# Use an official Maven image with JDK 17 to build the project
FROM maven:3.8.7-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Use Tomcat for the final run
FROM tomcat:10-jdk17
WORKDIR /usr/local/tomcat/webapps/
COPY --from=build /app/target/app.war ROOT.war

# Set environment variables correctly
ENV CATALINA_OPTS="-Dspring.main.allow-circular-references=true -Dspring.devtools.restart.enabled=false -Dspring.aop.proxy-target-class=false"

# Ensure Render picks the correct port
ENV PORT=8081
EXPOSE 8081

# Start Tomcat with environment variables
CMD ["sh", "-c", "catalina.sh run"]
