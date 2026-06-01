ALTER TABLE fuel_logs
ADD COLUMN notes TEXT,
ADD COLUMN payment_method_id BIGINT;

ALTER TABLE fuel_logs
ADD CONSTRAINT fk_payment_method
FOREIGN KEY (payment_method_id) REFERENCES payment_methods(id);