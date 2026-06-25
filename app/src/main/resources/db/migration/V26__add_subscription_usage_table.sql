CREATE TABLE subscription_usage (
    id BIGSERIAL PRIMARY KEY,

    user_subscription_id BIGINT NOT NULL,

    period_start TIMESTAMP NOT NULL,
    period_end TIMESTAMP NOT NULL,

    scans_used INTEGER NOT NULL DEFAULT 0,
    audios_used INTEGER NOT NULL DEFAULT 0,

    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_usage_subscription
        FOREIGN KEY (user_subscription_id)
        REFERENCES user_subscriptions(id)
        ON DELETE CASCADE,

    CONSTRAINT chk_scans_non_negative
        CHECK (scans_used >= 0),

    CONSTRAINT chk_audios_non_negative
        CHECK (audios_used >= 0),

    CONSTRAINT chk_valid_period
        CHECK (period_end > period_start)
);