CREATE TABLE transmission_types (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

ALTER TABLE vehicle_details ADD COLUMN transmission_type_id INTEGER;

ALTER TABLE vehicle_details ADD CONSTRAINT fk_transmission_type FOREIGN KEY (transmission_type_id) REFERENCES transmission_types(id);