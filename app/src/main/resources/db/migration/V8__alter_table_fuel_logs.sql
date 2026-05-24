ALTER TABLE fuel_logs
RENAME COLUMN liters TO gallons;

ALTER TABLE fuel_logs
ADD COLUMN gas_station VARCHAR(255);