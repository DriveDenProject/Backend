
-- =========================
-- VEHICLES
-- =========================
CREATE TABLE vehicles (
    id SERIAL PRIMARY KEY,
    brand VARCHAR(150) NOT NULL,
    model VARCHAR(150) NOT NULL,
    year INTEGER NOT NULL
);

-- =========================
-- USER_VEHICLES (N:M)
-- =========================
CREATE TABLE user_vehicles (
    user_id INTEGER NOT NULL,
    vehicle_id INTEGER NOT NULL,
    is_primary BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (user_id, vehicle_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(id) ON DELETE CASCADE
);

-- Solo 1 vehículo principal por usuario (PostgreSQL)
CREATE UNIQUE INDEX one_primary_vehicle_per_user
ON user_vehicles(user_id)
WHERE is_primary = TRUE;

-- =========================
-- FUEL TYPES
-- =========================
CREATE TABLE fuel_types (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- =========================
-- VEHICLE DETAILS (1:1)
-- =========================
CREATE TABLE vehicle_details (
    vehicle_id INTEGER PRIMARY KEY,
    fuel_type_id INTEGER NOT NULL,
    current_km INTEGER NOT NULL,
    last_technical_inspection DATE,
    last_soat DATE,
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(id) ON DELETE CASCADE,
    FOREIGN KEY (fuel_type_id) REFERENCES fuel_types(id)
);

-- =========================
-- PART CATEGORIES
-- =========================
CREATE TABLE part_categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL UNIQUE
);

-- =========================
-- PARTS
-- =========================
CREATE TABLE parts (
    id SERIAL PRIMARY KEY,
    category_id INTEGER NOT NULL,
    name VARCHAR(200) NOT NULL,
    brand VARCHAR(100),
    FOREIGN KEY (category_id) REFERENCES part_categories(id)
);

-- =========================
-- REPAIRS
-- =========================
CREATE TABLE repairs (
    id SERIAL PRIMARY KEY,
    vehicle_id INTEGER NOT NULL,
    repair_date TIMESTAMP NOT NULL,
    description TEXT,
    total_cost DECIMAL(10,2),
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(id) ON DELETE CASCADE
);

-- =========================
-- REPAIR PARTS (N:M)
-- =========================
CREATE TABLE repair_parts (
    repair_id INTEGER NOT NULL,
    part_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    cost DECIMAL(10,2) NOT NULL,
    warranty_expiration DATE,
    part_expiration DATE,
    PRIMARY KEY (repair_id, part_id),
    FOREIGN KEY (repair_id) REFERENCES repairs(id) ON DELETE CASCADE,
    FOREIGN KEY (part_id) REFERENCES parts(id)
);

-- =========================
-- ODOMETER LOGS
-- =========================
CREATE TABLE odometer_logs (
    id SERIAL PRIMARY KEY,
    vehicle_id INTEGER NOT NULL,
    km INTEGER NOT NULL,
    recorded_at TIMESTAMP NOT NULL,
    note VARCHAR(200),
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(id) ON DELETE CASCADE
);

CREATE INDEX idx_odometer_vehicle ON odometer_logs(vehicle_id);

-- =========================
-- FUEL LOGS
-- =========================
CREATE TABLE fuel_logs (
    id SERIAL PRIMARY KEY,
    vehicle_id INTEGER NOT NULL,
    liters DECIMAL(10,2) NOT NULL,
    price_total DECIMAL(10,2) NOT NULL,
    price_per_liter DECIMAL(10,2),
    km_at_fill INTEGER NOT NULL,
    filled_at TIMESTAMP NOT NULL,
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(id) ON DELETE CASCADE
);

CREATE INDEX idx_fuel_vehicle ON fuel_logs(vehicle_id);

-- =========================
-- MAINTENANCE SCHEDULES
-- =========================
CREATE TABLE maintenance_schedules (
    id SERIAL PRIMARY KEY,
    vehicle_id INTEGER NOT NULL,
    service_name VARCHAR(150) NOT NULL,
    interval_km INTEGER,
    interval_months INTEGER,
    last_service_km INTEGER,
    last_service_date DATE,
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(id) ON DELETE CASCADE
);

CREATE INDEX idx_maintenance_vehicle ON maintenance_schedules(vehicle_id);