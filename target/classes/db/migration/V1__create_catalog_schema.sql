CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL UNIQUE,
    description VARCHAR(400),
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE sellers (
    id BIGSERIAL PRIMARY KEY,
    external_id VARCHAR(80) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    rating NUMERIC(3,2),
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    sku VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(250) NOT NULL,
    description VARCHAR(2000),
    price NUMERIC(12,2) NOT NULL,
    stock_quantity INTEGER NOT NULL,
    active BOOLEAN NOT NULL,
    category_id BIGINT NOT NULL REFERENCES categories(id),
    seller_id BIGINT NOT NULL REFERENCES sellers(id),
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);
