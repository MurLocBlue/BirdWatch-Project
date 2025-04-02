#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}🚀 Starting test execution script...${NC}"

# Function to check if PostgreSQL container is running
check_postgres() {
    if docker ps | grep -q "bird-watch-db"; then
        return 0 # Container is running
    else
        return 1 # Container is not running
    fi
}

# Function to wait for PostgreSQL to be ready
wait_for_postgres() {
    echo -e "${YELLOW}🔍 Waiting for PostgreSQL to be ready...${NC}"
    while ! docker exec bird-watch-db pg_isready -U postgres > /dev/null 2>&1; do
        echo -n "."
        sleep 1
    done
    echo -e "\n${GREEN}✅ PostgreSQL is ready!${NC}"
}

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo -e "${RED}❌ Error: Docker is not running. Please start Docker first.${NC}"
    exit 1
fi

# Check if PostgreSQL container exists but is stopped
if docker ps -a | grep -q "bird-watch-db"; then
    if ! check_postgres; then
        echo -e "${YELLOW}🔄 PostgreSQL container exists but is stopped. Starting it...${NC}"
        docker start bird-watch-db
        wait_for_postgres
    else
        echo -e "${GREEN}✅ PostgreSQL container is already running.${NC}"
    fi
else
    # PostgreSQL container doesn't exist, start it using docker-compose
    echo -e "${YELLOW}🔄 PostgreSQL container not found. Starting using docker-compose...${NC}"
    docker-compose up -d postgres
    wait_for_postgres
fi

# Change to the api directory
cd api || exit 1

# Run the tests
echo -e "${YELLOW}🔍 Running tests...${NC}"
if mvn test; then
    echo -e "${GREEN}✅ Tests completed successfully!${NC}"
    exit 0
else
    echo -e "${RED}❌ Tests failed!${NC}"
    exit 1
fi 