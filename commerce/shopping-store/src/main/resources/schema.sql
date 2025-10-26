DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS categpries;
DROP TABLE IF EXISTS states;
DROP TABLE IF EXISTS quantity;

CREATE TABLE IF NOT EXISTS products (
    product_Id UUID PRIMARY KEY,
    product_name VARCHAR NOT NULL,
    description VARCHAR NOT NULL,
    image_src VARCHAR,
    quantity_state VARCHAR NOT NULL,
    product_state VARCHAR NOT NULL,
    product_category VARCHAR NOT NULL,
    price DECIMAL
);