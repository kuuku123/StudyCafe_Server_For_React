#!/bin/bash

# Exit immediately if a command exits with a non-zero status
set -e

# Step 1: Clean and package the Maven project without running tests
echo "Running Maven clean and package..."
mvn clean package -DskipTests

# Step 2: Build the Docker image
echo "Building Docker image..."
docker build -t studycafe_server_for_react .

# Step 3: Tag the Docker image
echo "Tagging Docker image..."
docker tag studycafe_server_for_react:latest kuuku123/studycafe_server_for_react:latest

# Step 4: Push the Docker image to Docker Hub (if logged in)
if docker info | grep -q "Username"; then
  echo "Pushing Docker image to Docker Hub..."
  docker push kuuku123/studycafe_server_for_react:latest
else
  echo "Not logged into Docker. Skipping Docker image push."
fi

# Step 5: Bring down any existing Docker Compose services
echo "Stopping and removing existing Docker containers..."
docker-compose -f deploy.yml down

# Step 6: Start Docker Compose services
echo "Starting Docker Compose services..."
docker-compose -f deploy.yml up

echo "Deployment complete!"

