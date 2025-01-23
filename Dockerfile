# We use a base image with Java 23 and Maven
FROM maven:3.9.9-eclipse-temurin-23 AS build

# Create and set up a working directory
WORKDIR /app

# Copy pom.xml to preload Maven dependencies
COPY pom.xml .
RUN mvn dependency:resolve

# Copy application code
COPY src ./src
