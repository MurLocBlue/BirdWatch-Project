#!/bin/bash

echo "🚀 Building and starting BirdWatch containers..."

# Build the containers
echo "📦 Building containers..."
docker-compose build

# Start the containers in detached mode
echo "▶️ Starting containers..."
docker-compose up -d

# Check if containers are running
echo "🔍 Checking container status..."
docker-compose ps

echo "✅ Done! BirdWatch is now running."
echo "📝 API is available at http://localhost:8080"
echo "💾 Database is available at localhost:5432" 