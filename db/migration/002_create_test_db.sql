-- Create the test database
CREATE DATABASE "bird-watch-test";

-- Connect to the test database
\c "bird-watch-test"

-- Create the same schema as the main database
CREATE TABLE birds (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    color VARCHAR(50) NOT NULL,
    height DOUBLE PRECISION NOT NULL,
    weight DOUBLE PRECISION NOT NULL,
    created_at TIMESTAMP
);

CREATE TABLE sightings (
    id BIGSERIAL PRIMARY KEY,
    bird_id BIGINT NOT NULL,
    location VARCHAR(100) NOT NULL,
    sighting_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP,
    FOREIGN KEY (bird_id) REFERENCES birds(id)
); 