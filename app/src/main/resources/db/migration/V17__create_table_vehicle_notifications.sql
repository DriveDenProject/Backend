-- =========================
-- VEHICLE NOTIFICATIONS
-- =========================
CREATE TABLE vehicle_notifications (
    id SERIAL PRIMARY KEY,

    vehicle_id INTEGER NOT NULL,
    category_id INTEGER NOT NULL,

    service_name VARCHAR(150) NOT NULL,
    description TEXT,

    start_date DATE NOT NULL,
    due_date DATE NOT NULL,

    reminder_frequency_days INTEGER NOT NULL,

    priority VARCHAR(20)
        NOT NULL DEFAULT 'MEDIUM'
        CHECK (priority IN (
            'LOW',
            'MEDIUM',
            'HIGH',
            'URGENT'
        )),

    status VARCHAR(20)
        NOT NULL DEFAULT 'PENDING'
        CHECK (status IN (
            'PENDING',
            'COMPLETED',
            'OVERDUE',
            'CANCELLED'
        )),

    is_recurring BOOLEAN NOT NULL DEFAULT FALSE,

    recurrence_interval_days INTEGER,

    last_notification_sent TIMESTAMP,

    notify_before_days INTEGER NOT NULL DEFAULT 0,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (vehicle_id)
        REFERENCES vehicles(id)
        ON DELETE CASCADE,

    FOREIGN KEY (category_id)
        REFERENCES maintenance_categories(id)
        ON DELETE RESTRICT,

    CHECK (
        due_date >= start_date
    ),

    CHECK (
        recurrence_interval_days IS NULL
        OR recurrence_interval_days > 0
    )
);

-- =========================
-- INDEXES
-- =========================
CREATE INDEX idx_vehicle_notifications_vehicle
ON vehicle_notifications(vehicle_id);

CREATE INDEX idx_vehicle_notifications_due_date
ON vehicle_notifications(due_date);

CREATE INDEX idx_vehicle_notifications_status
ON vehicle_notifications(status);

CREATE INDEX idx_vehicle_notifications_recurring
ON vehicle_notifications(is_recurring);