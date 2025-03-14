# Start with a base image that has Java
FROM azul/zulu-openjdk-alpine:21

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from your local machine to the Docker image
COPY target/study-service.jar /app/study-service.jar

# Expose the port on which the Spring Boot app will run
EXPOSE 8081

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "/app/study-service.jar"]
