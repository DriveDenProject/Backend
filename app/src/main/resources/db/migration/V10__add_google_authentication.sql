ALTER TABLE users
ALTER COLUMN pwd DROP NOT NULL;

ALTER TABLE users
ADD COLUMN google_id VARCHAR(255) UNIQUE,
ADD COLUMN profile_picture VARCHAR(500),
ADD COLUMN email_verified BOOLEAN NOT NULL DEFAULT FALSE;

CREATE TABLE user_auth_providers (
    user_id INTEGER NOT NULL,
    provider VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, provider),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

INSERT INTO user_auth_providers (user_id, provider)
SELECT id, 'LOCAL'
FROM users;
