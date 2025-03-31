-- Insert example birds
INSERT INTO birds (name, color, weight, height) VALUES
('Pormubel', 'Gri cu Alb', 8.50, 11.00),
('Mierla', 'Negru', 95.00, 25.00),
('Ciocarlie', 'Maro', 25.00, 18.00),
('Corb', 'Negru', 350.00, 35.00),
('Vrabie', 'Maro si Gri', 30.00, 16.00);

-- Insert example sightings
INSERT INTO sightings (bird_id, location, sighting_date) VALUES
(1, 'Parcul Cismigiu', '2024-03-30 08:00:00'),
(2, 'Parcul Herastrau', '2024-03-30 09:15:00'),
(3, 'Parcul Tineretului', '2024-03-29 14:30:00'),
(4, 'Parcul Cismigiu', '2024-03-28 11:00:00'),
(5, 'Parcul Herastrau', '2024-03-27 10:30:00'); 