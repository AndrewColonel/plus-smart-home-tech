DROP TABLE IF EXISTS carts;

CREATE TABLE IF NOT EXISTS carts (
    product_Id UUID PRIMARY KEY,
    product_name VARCHAR NOT NULL,
    description VARCHAR NOT NULL,
    image_src VARCHAR,
    quantity_state VARCHAR NOT NULL,
    product_state VARCHAR NOT NULL,
    product_category VARCHAR NOT NULL,
    price DECIMAL
);