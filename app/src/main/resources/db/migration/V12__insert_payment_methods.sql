
INSERT INTO payment_methods (
    name,
    code,
    description,
    is_active
)
VALUES
(
    'Tarjeta de crédito',
    'CREDIT_CARD',
    'Pago con Visa, Mastercard, American Express',
    TRUE
),
(
    'Tarjeta débito',
    'DEBIT_CARD',
    'Pago con tarjeta débito bancaria',
    TRUE
),
(
    'PSE',
    'PSE',
    'Pago desde cuenta bancaria mediante PSE',
    TRUE
),
(
    'Nequi',
    'NEQUI',
    'Pago usando cuenta Nequi',
    TRUE
),
(
    'Daviplata',
    'DAVIPLATA',
    'Pago usando Daviplata',
    TRUE
),
(
    'PayPal',
    'PAYPAL',
    'Pago internacional con PayPal',
    TRUE
),
(
    'Transferencia bancaria',
    'BANK_TRANSFER',
    'Pago mediante transferencia bancaria',
    TRUE
),
(
    'Efectivo',
    'CASH',
    'Pago en efectivo en puntos autorizados',
    FALSE
);