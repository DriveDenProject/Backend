INSERT INTO subscription_plans (
    code,
    name,
    description,
    monthly_price,
    yearly_price,
    currency,
    max_vehicles,
    max_scan_imgs,
    max_audios
)
VALUES (
    'FREE',
    'DriveDen Free',
    'Basic plan for single vehicle owners',
    0.00,
    0.00,
    'USD',
    1,
    5,
    5
    );

INSERT INTO subscription_plans (
    code,
    name,
    description,
    monthly_price,
    yearly_price,
    currency,
    max_vehicles,
    max_scan_imgs,
    max_audios
)
VALUES (
    'PLUS',
    'DriveDen Plus',
    'Advanced plan for multi-vehicle users',
    4.99,
    49.99,
    'USD',
    5,
    50,
    30
);


INSERT INTO subscription_plans (
    code,
    name,
    description,
    monthly_price,
    yearly_price,
    currency,
    max_vehicles,
    max_scan_imgs,
    max_audios
)
VALUES (
    'PRO',
    'DriveDen Pro',
    'Professional unlimited plan',
    9.99,
    99.99,
    'USD',
    9999,
    9999,
    9999
);