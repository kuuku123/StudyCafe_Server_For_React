#!/bin/bash

# Exit immediately if a command exits with a non-zero status
set -e

# Step 0: Remove the existing Docker container if it exists
if docker ps -a --format '{{.Names}}' | grep -Eq "^react-apache\$"; then
  echo "Stopping and removing existing 'react-apache' container..."
  docker stop react-apache
  docker rm react-apache
fi

# Step 1: Build the React app
echo "Building React app..."
npm run build

# Step 2: Build the Docker image
echo "Building Docker image..."
docker build -t react-apache-app .

# Step 3: Run the Docker container
echo "Running Docker container..."
docker run -d -p 3000:80 --name react-apache react-apache-app

echo "Deployment completed! Your app is running at http://localhost:3000"

