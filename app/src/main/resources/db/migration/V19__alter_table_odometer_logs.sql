BEGIN;

-- 1. Cambiar vehicle_id a BIGINT
ALTER TABLE odometer_logs
ALTER COLUMN vehicle_id TYPE BIGINT;

-- 2. Agregar CHECK para km >= 0
ALTER TABLE odometer_logs
ADD CONSTRAINT chk_odometer_km_positive
CHECK (km >= 0);

-- 3. Agregar columna source
-- Temporalmente nullable para no romper datos existentes
ALTER TABLE odometer_logs
ADD COLUMN source VARCHAR(30);

-- 4. Agregar source_id
ALTER TABLE odometer_logs
ADD COLUMN source_id BIGINT;

-- 5. Agregar created_at
ALTER TABLE odometer_logs
ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT NOW();

-- 6. Poner default a recorded_at
ALTER TABLE odometer_logs
ALTER COLUMN recorded_at SET DEFAULT NOW();

-- 7. Poblar source en datos antiguos
UPDATE odometer_logs
SET source = 'MANUAL'
WHERE source IS NULL;

-- 8. Hacer source obligatorio
ALTER TABLE odometer_logs
ALTER COLUMN source SET NOT NULL;

-- 9. Renombrar foreign key (opcional)
-- Buscar el nombre real de la FK antes si fue autogenerada
ALTER TABLE odometer_logs
DROP CONSTRAINT IF EXISTS odometer_logs_vehicle_id_fkey;

ALTER TABLE odometer_logs
ADD CONSTRAINT fk_odometer_vehicle
FOREIGN KEY (vehicle_id)
REFERENCES vehicles(id)
ON DELETE CASCADE;

-- 10. Crear índices recomendados
CREATE INDEX IF NOT EXISTS idx_odometer_vehicle
ON odometer_logs(vehicle_id);

CREATE INDEX IF NOT EXISTS idx_odometer_vehicle_date
ON odometer_logs(vehicle_id, recorded_at);

CREATE INDEX IF NOT EXISTS idx_odometer_source
ON odometer_logs(source);

COMMIT;