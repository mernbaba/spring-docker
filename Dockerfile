# Use the official Maven image to build the project
FROM maven:3.8.5-openjdk-17-slim AS builder

# Set the working directory
WORKDIR /app

# Copy the Maven project files
COPY pom.xml .
COPY src ./src

# Build the project and create the jar file
RUN mvn clean package -DskipTests

# Use the official OpenJDK image to run the application
FROM openjdk:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=builder /app/target/jwt-demo-0.0.1-SNAPSHOT.jar fitmark-server.jar

RUN ls

# Set the entry point to run the jar file
ENTRYPOINT ["java", "-jar", "fitmark-server.jar"]

# Expose the application port
EXPOSE 5000
