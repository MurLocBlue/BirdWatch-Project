#!/bin/bash

echo "🛑 Stopping BirdWatch containers..."

# Stop and remove containers
echo "⏹️ Stopping containers..."
docker-compose down

# Check if containers are stopped
echo "🔍 Checking container status..."
docker-compose ps

echo "✅ Done! BirdWatch containers have been stopped." 