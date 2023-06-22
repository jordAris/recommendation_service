# Use the official Maven image as the base image
FROM maven:3.8.4-openjdk-17-slim AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and download the dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code
COPY src/ ./src/

# Build the application
RUN mvn package -DskipTests

# Create a new stage for the final image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the previous stage
COPY --from=build /app/target/your-application.jar ./your-application.jar

# Expose the necessary ports
EXPOSE 8080

# Set the command to run the application
CMD ["java", "-jar", "your-application.jar"]
