CREATE TABLE subscription_plans ( 
    id BIGSERIAL PRIMARY KEY, 
    code VARCHAR(50) NOT NULL UNIQUE, 
    name VARCHAR(120) NOT NULL, 
    description TEXT, 
    monthly_price DECIMAL(10,2), 
    yearly_price DECIMAL(10,2), 
    currency VARCHAR(10) DEFAULT 'USD', 
    max_vehicles INTEGER, 
    max_scan_imgs INTEGER, 
    max_audios INTEGER, 
    is_active BOOLEAN DEFAULT TRUE, 
    created_at TIMESTAMP NOT NULL DEFAULT NOW(), 
    updated_at TIMESTAMP NOT NULL DEFAULT NOW() 
);

CREATE TABLE user_subscriptions (
    id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL,
    subscription_plan_id BIGINT NOT NULL,

    provider VARCHAR(30) NOT NULL,
    provider_subscription_id VARCHAR(255),

    status VARCHAR(30) NOT NULL,

    starts_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP,

    auto_renew BOOLEAN DEFAULT TRUE,
    is_trial BOOLEAN DEFAULT FALSE,

    cancelled_at TIMESTAMP,
    grace_period_until TIMESTAMP,

    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),

    CONSTRAINT fk_subscription_user
        FOREIGN KEY (user_id)
        REFERENCES users(id),

    CONSTRAINT fk_subscription_plan
        FOREIGN KEY (subscription_plan_id)
        REFERENCES subscription_plans(id)
);

CREATE TABLE payment_transactions (
    id BIGSERIAL PRIMARY KEY,

    user_subscription_id BIGINT NOT NULL,

    provider VARCHAR(30) NOT NULL,
    provider_transaction_id VARCHAR(255),

    amount DECIMAL(10,2) NOT NULL,
    currency VARCHAR(10) NOT NULL,

    payment_status VARCHAR(30) NOT NULL,

    paid_at TIMESTAMP,

    raw_provider_response JSONB,

    failure_reason TEXT,

    created_at TIMESTAMP DEFAULT NOW(),

    CONSTRAINT fk_payment_subscription
        FOREIGN KEY (user_subscription_id)
        REFERENCES user_subscriptions(id)
);

CREATE TABLE subscription_features (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(100) UNIQUE NOT NULL,
    description TEXT
);

CREATE TABLE plan_features (
    subscription_plan_id BIGINT NOT NULL,
    feature_id BIGINT NOT NULL,

    PRIMARY KEY (
        subscription_plan_id,
        feature_id
    ),

    FOREIGN KEY (subscription_plan_id)
        REFERENCES subscription_plans(id),

    FOREIGN KEY (feature_id)
        REFERENCES subscription_features(id)
);

CREATE INDEX idx_user_subscription_user_status
ON user_subscriptions(user_id, status);

CREATE INDEX idx_payment_subscription
ON payment_transactions(user_subscription_id);