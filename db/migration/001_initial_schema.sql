-- Create birds table
CREATE TABLE birds (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    color VARCHAR(50) NOT NULL,
    weight DECIMAL(5, 2) NOT NULL,  -- Weight in grams
    height DECIMAL(5, 2) NOT NULL,  -- Height in centimeters
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create sightings table
CREATE TABLE sightings (
    id SERIAL PRIMARY KEY,
    bird_id INTEGER REFERENCES birds(id),
    location VARCHAR(100) NOT NULL,
    sighting_date TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better query performance
CREATE INDEX idx_sightings_bird_id ON sightings(bird_id);
CREATE INDEX idx_sightings_sighting_date ON sightings(sighting_date); 