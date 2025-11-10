DROP TABLE IF EXISTS order_items;
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

CREATE TABLE IF NOT EXISTS order_items(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    order_id UUID,
    delivery_id UUID,
    product_id UUID,
    quantity BIGINT CHECK (quantity >= 0)
   );