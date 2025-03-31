# BirdWatch Project 🦜

A web application for tracking and managing bird sightings.

## Getting Started 🚀

1. Pull / Download project from Git
2. Open a Terminal in the root of the project and run `docker-compose build`
3. Once the project is built, run `docker-compose up -d` to start the Posgres Database and Spring API Service

To stop all of the containers, simply run `docker-compose down` or if you want to remove all the containers run `docker-compose down -v`

## API Documentation 📚

### Birds Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/api/birds` | Get all birds |
| GET    | `/api/birds/{id}` | Get a specific bird |
| POST   | `/api/birds` | Create a new bird |
| PUT    | `/api/birds/{id}` | Update an existing bird |
| DELETE | `/api/birds/{id}` | Delete a bird |

### Sightings Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/api/sightings` | Get all sightings |
| GET    | `/api/sightings/{id}` | Get a specific sighting |
| POST   | `/api/sightings` | Create a new sighting |
| PUT    | `/api/sightings/{id}` | Update an existing sighting |
| DELETE | `/api/sightings/{id}` | Delete a sighting |
