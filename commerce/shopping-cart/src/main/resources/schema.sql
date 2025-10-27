--DROP TABLE IF EXISTS cart_product;
DROP TABLE IF EXISTS shopping_cart CASCADE;

CREATE TABLE IF NOT EXISTS shopping_cart(
    cart_Id UUID PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS cart_product(
  cart_id UUID NOT NULL REFERENCES shopping_cart(cart_Id) ON DELETE CASCADE,
  product_id UUID NOT NULL,
  quantity BIGINT NOT NULL CHECK (quantity > 0),
  PRIMARY KEY (cart_id, product_id)
);