DROP TABLE IF EXISTS delivery;

CREATE TABLE IF NOT EXISTS delivery(
    delivery_id UUID PRIMARY KEY,
    -- from_address --
    from_country VARCHAR,
    from_city VARCHAR,
    from_street VARCHAR,
    from_house VARCHAR,
    from_flat VARCHAR,
        -- to_address --
    to_country VARCHAR,
    to_city VARCHAR,
    to_street VARCHAR,
    to_house VARCHAR,
    to_flat VARCHAR,
    order_id UUID NOT NULL,
    delivery_state VARCHAR NOT NULL,
    UNIQUE(order_id)
 );