CREATE TABLE IF NOT EXISTS orders
(
    id         uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    promo_code varchar(100),
    created_at timestamp    NOT NULL,
    created_by varchar(100) NOT NULL
);
