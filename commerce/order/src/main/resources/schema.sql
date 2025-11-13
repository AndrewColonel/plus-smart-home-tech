DROP TABLE IF EXISTS order_products;
DROP TABLE IF EXISTS orders CASCADE;

CREATE TABLE IF NOT EXISTS orders(
    order_id UUID PRIMARY KEY,
    shopping_cart_id UUID NOT NULL,
    payment_id UUID,
    delivery_id UUID,
    state VARCHAR,
    delivery_weight DECIMAL(6, 2) CHECK (delivery_weight > 0),
    delivery_volume DECIMAL(6, 2) CHECK (delivery_volume > 0),
    fragile BOOLEAN,
    total_price DECIMAL(6, 2) CHECK (total_price > 0),
    delivery_price DECIMAL(6, 2) CHECK (delivery_price > 0),
    product_price DECIMAL(6, 2) CHECK (product_price > 0),
    -- delivery_to_address --
    country VARCHAR,
    city VARCHAR,
    street VARCHAR,
    house VARCHAR,
    flat VARCHAR,
    UNIQUE(shopping_cart_id, payment_id, delivery_id)
);

CREATE TABLE IF NOT EXISTS order_products(
    order_id UUID NOT NULL REFERENCES orders(order_id) ON DELETE CASCADE,
    product_id UUID NOT NULL,
    quantity BIGINT NOT NULL CHECK (quantity >= 0),
    PRIMARY KEY (order_id, product_id)
);