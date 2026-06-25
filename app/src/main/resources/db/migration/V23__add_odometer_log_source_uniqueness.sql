CREATE UNIQUE INDEX IF NOT EXISTS ux_odometer_logs_source_source_id
ON odometer_logs(source, source_id)
WHERE source_id IS NOT NULL;
