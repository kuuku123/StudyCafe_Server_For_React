#!/bin/bash

# Delete existing Kubernetes resources defined in YAML files in the current directory
kubectl delete -f .
sleep 15

# Remove all YAML files in the current directory
rm -rf *.yaml
sleep 1

# Convert docker-compose.yml to Kubernetes YAML files
kompose convert
sleep 1

# Copy spring-app-ingress.yaml from the ingress directory to the current directory
cp ingress/spring-app-ingress.yaml .

# Apply the new Kubernetes configuration
kubectl apply -f .

