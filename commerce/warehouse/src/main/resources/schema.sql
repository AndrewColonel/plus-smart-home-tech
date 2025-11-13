DROP TABLE IF EXISTS order_booking;
DROP TABLE IF EXISTS booking;
DROP TABLE IF EXISTS warehouse_items;

CREATE TABLE IF NOT EXISTS warehouse_items(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    product_id UUID NOT NULL UNIQUE,
    fragile BOOLEAN NOT NULL,
    width DECIMAL(5, 2) NOT NULL CHECK (width > 0),
    height DECIMAL(5, 2) NOT NULL CHECK (height > 0),
    depth DECIMAL(5, 2) NOT NULL CHECK (depth > 0),
    weight DECIMAL(6, 2) NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity >= 0)
 );

CREATE TABLE IF NOT EXISTS booking(
    booking_id UUID PRIMARY KEY,
    order_id UUID,
    delivery_id UUID,
    -- warehouse_from_address --
    country VARCHAR,
    city VARCHAR,
    street VARCHAR,
    house VARCHAR,
    flat VARCHAR,
    UNIQUE(order_id, delivery_id)
);

CREATE TABLE IF NOT EXISTS order_booking(
    booking_id UUID NOT NULL REFERENCES booking(booking_id) ON DELETE CASCADE,
    product_id UUID NOT NULL,
    quantity BIGINT NOT NULL CHECK (quantity >= 0),
    PRIMARY KEY (booking_id, product_id)
);