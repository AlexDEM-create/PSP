ALTER TABLE incoming_payments
    ADD COLUMN bank VARCHAR(50) NOT NULL DEFAULT 'SBER';
ALTER TABLE incoming_payments
    ALTER COLUMN bank DROP DEFAULT;
