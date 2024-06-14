ALTER TABLE payment_methods
    ADD COLUMN account_last_four_digits VARCHAR(4) NOT NULL DEFAULT '0000';
ALTER TABLE payment_methods
    ALTER COLUMN account_last_four_digits DROP DEFAULT;
