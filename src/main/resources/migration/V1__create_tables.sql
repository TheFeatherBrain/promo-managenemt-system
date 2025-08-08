CREATE TABLE IF NOT EXISTS promo_codes (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    code varchar(100) NOT NULL UNIQUE,
    discount_value numeric(10,2),
    discount_percentage numeric(2,0),
    amount_type varchar(20) NOT NULL,
    expiry_date timestamp with time zone,
    usage_limit integer,
    status smallint NOT NULL,
    created_at timestamp NOT NULL,
    created_by varchar(100) NOT NULL,
    updated_at timestamp,
    update_by varchar(100)
);
