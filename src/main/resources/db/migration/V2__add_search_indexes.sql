CREATE INDEX idx_products_name ON products (name);
CREATE INDEX idx_products_category_id ON products (category_id);
CREATE INDEX idx_products_seller_id ON products (seller_id);
CREATE INDEX idx_products_price ON products (price);
CREATE INDEX idx_products_active_stock ON products (active, stock_quantity);
