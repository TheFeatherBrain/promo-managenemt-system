INSERT INTO promo_code (code, discount_percentage, expiry_date, usage_limit, status, created_at, created_by)
VALUES ('WELCOME10', 10, '2025-12-31 05:24:40.731', 100, 1, now(), 'Flyway'),
       ('WELCOME20', 20, '2025-08-01 05:24:40.731', 50, 2, now(), 'Flyway'),
       ('WELCOME30', 30, '2025-12-31 05:24:40.731', 100, 3, now(), 'Flyway');

INSERT INTO promo_code (code, discount_value, expiry_date, usage_limit, status, usages, created_at, created_by)
VALUES ('HELLO10', 10, '2025-12-31 05:24:40.731', 100, 1, 99, now(), 'Flyway'),
       ('HELLO20', 20, '2025-08-01 05:24:40.731', 50, 2, 40, now(), 'Flyway'),
       ('HELLO30', 30, '2025-12-31 05:24:40.731', 100, 3, 100, now(), 'Flyway');
