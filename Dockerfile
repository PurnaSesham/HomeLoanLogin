# Use a lightweight JDK base image
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /HomeLoanApp

# Copy the JAR file into the container
COPY build/libs/HomeLoanLogin-0.0.1-SNAPSHOT.jar HomeLoanApp-0.0.1.jar

# Expose the application's port
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "HomeLoanApp-0.0.1.jar"]