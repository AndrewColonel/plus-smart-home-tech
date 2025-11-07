DROP TABLE IF EXISTS payment;

CREATE TABLE IF NOT EXISTS payment(
    payment_id UUID PRIMARY KEY,
    total_payment DECIMAL(6, 2) CHECK (total_payment > 0),
    delivery_total DECIMAL(6, 2) CHECK (delivery_total > 0),
    fee_total DECIMAL(6, 2) CHECK (fee_total > 0)
);