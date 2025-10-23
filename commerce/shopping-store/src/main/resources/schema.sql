DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS categpries;
DROP TABLE IF EXISTS states;
DROP TABLE IF EXISTS quantity;




--CREATE TABLE IF NOT EXISTS categpries (
--    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
--    product_category VARCHAR,
--    UNIQUE(product_category)
--);
--
--CREATE TABLE IF NOT EXISTS states (
--    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
--    product_state VARCHAR,
--    UNIQUE(product_state)
--);
--
--CREATE TABLE IF NOT EXISTS quantity (
--    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
--    quantity_state VARCHAR,
--    UNIQUE(quantity_state)
--);

--CREATE TABLE IF NOT EXISTS products (
--    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
--    product_Id VARCHAR,
--    product_name VARCHAR NOT NULL,
--    description VARCHAR NOT NULL,
--    imageSrc VARCHAR,
--    quantity_state_id BIGINT NOT NULL REFERENCES quantity(id),
--    product_state_id BIGINT NOT NULL REFERENCES states(id),
--    product_category_id BIGINT NOT NULL REFERENCES categpries(id),
--    price DECIMAL
--);

CREATE TABLE IF NOT EXISTS products (
--    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    product_Id VARCHAR PRIMARY KEY,
    product_name VARCHAR NOT NULL,
    description VARCHAR NOT NULL,
    image_src VARCHAR,
    quantity_state VARCHAR NOT NULL,
    product_state VARCHAR NOT NULL,
    product_category VARCHAR NOT NULL,
    price DECIMAL
);