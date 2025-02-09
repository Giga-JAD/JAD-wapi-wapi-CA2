# Use an official Maven image with JDK 17 to build the project
FROM maven:3.8.7-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Use Tomcat for the final run
FROM tomcat:10-jdk17
WORKDIR /usr/local/tomcat/webapps/
COPY --from=build /app/target/app.war ROOT.war

# Set environment variables
ENV PORT=8081
ENV CATALINA_OPTS="-Dserver.port=${PORT}"

# Expose Render’s assigned port
EXPOSE 8081

# Start Tomcat ensuring the correct PORT is used
CMD ["sh", "-c", "catalina.sh run"]
