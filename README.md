# BirdWatch Project 🦜

A simple Eclipse plugin based application for tracking and managing bird sightings.

## Getting Started 🚀

You can start by either cloning the Git Repo or by heading to the [Latest Release](https://github.com/MurLocBlue/BirdWatch-Project/releases) page.

### Backend 🔧

#### Using Bash (Recommended)
1. Open a Bash Terminal in the root of the project
2. Make the start script executable: `chmod +x run_tests.sh`
3. Run the start script: `./start.sh`

To stop the application: `./stop.sh`

#### Manual Docker Commands
Alternatively, you can run the Docker commands manually:
1. Open a Terminal in the root of the project and run `docker-compose build`
2. Once the project is built, run `docker-compose up -d` to start the Posgres Database and Spring API Service

To stop and remove all of the containers, simply run `docker-compose down`

### Frontend 💻

1. Import the `plugin` and `plugin_api` projects into Eclipse
2. Configure your run configurations to start `birdwatch_plugin` as a `workbench` type application
3. Run the plugin

To stop the UI plugin, simply close the window

### Running Tests 🧪

#### Using the Test Script (Recommended)
1. Open a Bash Terminal in the root of the project
2. Make the test script executable: `chmod +x run_tests.sh`
3. Run the tests: `./run_tests.sh`

The script will:
- Check if Docker is running
- Ensure the PostgreSQL database container is up
- Wait for the database to be ready
- Run all tests using Maven

#### Manual Test Execution
Alternatively, you can run the tests manually:
1. Ensure Docker is running
2. Start the containers: `docker-compose up -d`
3. Wait for the database to be ready
4. Navigate to the api directory: `cd api`
5. Run the tests: `mvn test`

## Technologies Used 🛠️

### Backend
- Java 11 (Core programming language)
- Spring Boot 2.7.18 (Web application framework)
- Spring Data JPA (Database access and ORM)
- PostgreSQL Database (Relational database management system)
- Maven (Build automation and dependency management)
- Lombok (Java library for reducing boilerplate code)
- Jackson (JSON processing library)

### Frontend
- Eclipse RCP (Rich Client Platform)
- SWT (Standard Widget Toolkit)

### Infrastructure
- Docker (Containerization platform for packaging and running applications)
- Docker Compose (Tool for defining and running multi-container Docker applications)
- PostgreSQL (Open-source relational database system)
- Bash (Unix shell and command language for automation scripts)

### Testing
- JUnit 5 (Java unit testing framework)
- Mockito (Mocking framework for unit tests)
- Spring Boot Test (Integration testing framework for Spring Boot applications)
- Maven Surefire Plugin (Plugin for running tests during the build process)

## API Documentation 📚

### Birds Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/api/birds` | Get all birds |
| GET    | `/api/birds/{id}` | Get a specific bird |
| GET    | `/api/birds/search?name={bird_name}` | Search birds by name |
| GET    | `/api/birds/search?color={bird_color}` | Search birds by color |
| GET    | `/api/birds/search?name={bird_name}&color={bird_color}` | Search birds by name and color |
| POST   | `/api/birds` | Create a new bird |
| PUT    | `/api/birds/{id}` | Update an existing bird |
| DELETE | `/api/birds/{id}` | Delete a bird |

### Sightings Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/api/sightings` | Get all sightings |
| GET    | `/api/sightings/{id}` | Get a specific sighting |
| GET    | `/api/sightings/search?birdName={bird_name}` | Search sightings by bird name |
| GET    | `/api/sightings/search?location={location}` | Search sightings by location |
| GET    | `/api/sightings/search?startDate={ISO_format_startDate}` | Search sightings by interval starting from this date |
| GET    | `/api/sightings/search?endDate={ISO_format_endDate}` | Search sightings by interval ending on this date |
| GET    | `/api/sightings/search?startDate={ISO_format_startDate}&endDate={ISO_format_endDate}` | Search sightings by in the given time interval |
| GET    | `/api/sightings/search?birdName={bird_name}&location={location}&startDate={ISO_format_startDate}&endDate={ISO_format_endDate}` | Search sightings by bird name, location and time interval |
| POST   | `/api/sightings` | Create a new sighting |
| PUT    | `/api/sightings/{id}` | Update an existing sighting |
| DELETE | `/api/sightings/{id}` | Delete a sighting |


## Screenshots 📷

### Birds
![Birds](screenshots/birds.png)

### Sightings
![Sightings](screenshots/sightings.png)

## Notes 📄

This app was built as part of a hiring process for Spirent. The following [Specifications](specifications/iTestProgramExercise.pdf) were followed.