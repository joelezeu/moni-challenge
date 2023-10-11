# --- Build Stage ---

# Use the official Maven base image
FROM maven:3.8.1-jdk-11 as build

# Set the working directory in docker
WORKDIR /app

# Copy the pom.xml file
COPY pom.xml .

# Copy the rest of the source code
COPY src src/

# Package the application
RUN mvn clean package

# --- Package Stage ---

# Use the official OpenJDK base image
FROM openjdk:11-jre-slim

# Set the working directory in docker
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Run the JAR file
ENTRYPOINT ["java","-jar","app.jar"]
