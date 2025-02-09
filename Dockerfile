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
ENV PORT=8081
ENV CATALINA_OPTS="$CATALINA_OPTS -Dserver.port=$PORT"

# Ensure Render picks the correct port
EXPOSE 8081

# Start Tomcat with correct CATALINA_OPTS
CMD catalina.sh run
