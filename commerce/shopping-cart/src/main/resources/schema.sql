DROP TABLE IF EXISTS user_carts;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE IF NOT EXISTS users(
    cart_Id UUID PRIMARY KEY,
    user_name VARCHAR NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_name)
);

CREATE TABLE IF NOT EXISTS user_carts(
    cart_id UUID NOT NULL REFERENCES users(cart_Id) ON DELETE CASCADE,
    product_id UUID NOT NULL,
    quantity BIGINT NOT NULL CHECK (quantity > 0),
    PRIMARY KEY (cart_id, product_id)
);