CREATE TYPE vehicle_type_enum AS ENUM ('CAR', 'MOTORCYCLE');

ALTER TABLE vehicles
ADD COLUMN vehicle_type vehicle_type_enum NOT NULL DEFAULT 'CAR';