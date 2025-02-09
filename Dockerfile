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
ENV PORT=8081
ENV CATALINA_OPTS="-Dserver.port=${PORT} -DPORT=${PORT}"

# Update Tomcat's server.xml to listen on all interfaces
RUN sed -i 's/Connector port="8080"/Connector port="'$PORT'" address="0.0.0.0"/g' /usr/local/tomcat/conf/server.xml

# Expose the correct port
EXPOSE 8081

# Start Tomcat
CMD ["sh", "-c", "catalina.sh run"]
