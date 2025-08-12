CREATE TABLE IF NOT EXISTS promo_code (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    code varchar(100) NOT NULL UNIQUE,
    discount_value numeric(10,2),
    discount_percentage numeric(2,0),
    expiry_date timestamp,
    usage_limit integer,
    status integer NOT NULL,
    created_at timestamp NOT NULL,
    created_by varchar(100) NOT NULL,
    updated_at timestamp,
    updated_by varchar(100)
);
