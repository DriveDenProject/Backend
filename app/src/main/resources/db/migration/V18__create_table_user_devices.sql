CREATE TABLE user_device_tokens (
    id SERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL,

    fcm_token TEXT NOT NULL UNIQUE,

    platform VARCHAR(20) NOT NULL,

    device_name VARCHAR(255),

    is_active BOOLEAN DEFAULT TRUE,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_user_device_tokens_user
ON user_device_tokens(user_id);