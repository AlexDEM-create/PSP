CREATE TABLE IF NOT EXISTS users (
    primary_key BIGSERIAL PRIMARY KEY,
    id VARCHAR(20) NOT NULL,
    login VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    role VARCHAR(50) NOT NULL,
    banned BOOLEAN NOT NULL,
    created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    deleted_date TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE IF NOT EXISTS trader_teams (
    primary_key BIGSERIAL PRIMARY KEY,
    id VARCHAR(20) NOT NULL,
    name VARCHAR(50) NOT NULL,
    user_id VARCHAR(20) NOT NULL,
    country VARCHAR(50) NOT NULL,
    leader_id VARCHAR(20) NOT NULL,
    trader_incoming_fee_rate NUMERIC(6,5) NOT NULL,
    trader_outgoing_fee_rate NUMERIC(6,5) NOT NULL,
    leader_incoming_fee_rate NUMERIC(6,5) NOT NULL,
    leader_outgoing_fee_rate NUMERIC(6,5) NOT NULL,
    verified BOOLEAN NOT NULL,
    incoming_online BOOLEAN NOT NULL,
    outgoing_online BOOLEAN NOT NULL,
    kicked_out BOOLEAN NOT NULL,
    created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    deleted_date TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE IF NOT EXISTS terminals (
    primary_key BIGSERIAL PRIMARY KEY,
    id VARCHAR(20) NOT NULL,
    trader_team_id VARCHAR(20) NOT NULL,
    verified BOOLEAN NOT NULL,
    enabled BOOLEAN NOT NULL,
    online BOOLEAN NOT NULL,
    model VARCHAR(50),
    operating_system VARCHAR(50),
    created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    deleted_date TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE IF NOT EXISTS stats (
    primary_key BIGSERIAL PRIMARY KEY,
    id VARCHAR(20) NOT NULL,
    entity_id VARCHAR(20) NOT NULL,
    entity_type VARCHAR(50) NOT NULL,
    today_outgoing_total NUMERIC(20,2) NOT NULL,
    today_incoming_total NUMERIC(20,2) NOT NULL,
    all_time_outgoing_total NUMERIC(20,2) NOT NULL,
    all_time_incoming_total NUMERIC(20,2) NOT NULL,
    created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_date TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS sms_payment_verifications (
    primary_key BIGSERIAL PRIMARY KEY,
    id VARCHAR(20) NOT NULL,
    incoming_payment_id VARCHAR(20) NOT NULL,
    recipient_card_last_four_digits VARCHAR(255) NOT NULL,
    sender_full_name VARCHAR(255) NOT NULL,
    amount NUMERIC(20,2) NOT NULL,
    amount_currency VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    dataJSON TEXT NOT NULL,
    created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS receipt_payment_verifications (
    primary_key BIGSERIAL PRIMARY KEY,
    id VARCHAR(20) NOT NULL,
    outgoing_payment_id VARCHAR(20) NOT NULL,
    data JSONB NOT NULL,
    uploaded_file BYTEA NOT NULL,
    created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS payment_methods (
    primary_key BIGSERIAL PRIMARY KEY,
    id VARCHAR(20) NOT NULL,
    number VARCHAR(50) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    currency VARCHAR(50) NOT NULL,
    bank VARCHAR(50) NOT NULL,
    trader_team_id VARCHAR(20) NOT NULL,
    terminal_id VARCHAR(20),
    enabled BOOLEAN NOT NULL,
    busy BOOLEAN NOT NULL,
    created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    deleted_date TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE IF NOT EXISTS outgoing_payments (
    primary_key BIGSERIAL PRIMARY KEY,
    id VARCHAR(20) NOT NULL,
    merchant_id VARCHAR(20) NOT NULL,
    trader_team_id VARCHAR(20) NOT NULL,
    payment_method_id VARCHAR(20) NOT NULL,
    amount NUMERIC(20,2) NOT NULL,
    currency VARCHAR(50) NOT NULL,
    recipient VARCHAR(50) NOT NULL,
    bank VARCHAR(50) NOT NULL,
    recipient_payment_method_type VARCHAR(50) NOT NULL,
    partner_payment_id VARCHAR(20) NOT NULL,
    current_state VARCHAR(50) NOT NULL,
    created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_date TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS merchants (
    primary_key BIGSERIAL PRIMARY KEY,
    id VARCHAR(20) NOT NULL,
    name VARCHAR(50) NOT NULL,
    user_id VARCHAR(20) NOT NULL,
    country VARCHAR(50) NOT NULL,
    incoming_fee_rate NUMERIC(6,5) NOT NULL,
    outgoing_fee_rate NUMERIC(6,5) NOT NULL,
    outgoing_traffic_stopped BOOLEAN NOT NULL,
    created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    deleted_date TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE IF NOT EXISTS incoming_payments (
    primary_key BIGSERIAL PRIMARY KEY,
    id VARCHAR(20) NOT NULL,
    merchant_id VARCHAR(20) NOT NULL,
    trader_team_id VARCHAR(20) NOT NULL,
    payment_method_id VARCHAR(20) NOT NULL,
    amount NUMERIC(20,2) NOT NULL,
    currency VARCHAR(50) NOT NULL,
    current_state VARCHAR(50) NOT NULL,
    created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_date TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS currency_exchanges (
    primary_key BIGSERIAL PRIMARY KEY,
    id VARCHAR(20) NOT NULL,
    source_currency VARCHAR(50) NOT NULL,
    target_currency VARCHAR(50) NOT NULL,
    buy_exchange_rate NUMERIC(20,5) NOT NULL,
    sell_exchange_rate NUMERIC(20,5) NOT NULL,
    updated_date TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS balances (
    primary_key BIGSERIAL PRIMARY KEY,
    id VARCHAR(20) NOT NULL,
    entity_id VARCHAR(20) NOT NULL,
    entity_type VARCHAR(50) NOT NULL,
    type VARCHAR(50) NOT NULL,
    current_balance NUMERIC(20,2) NOT NULL,
    currency VARCHAR(50) NOT NULL,
    created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    deleted_date TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE IF NOT EXISTS appeals (
    primary_key BIGSERIAL PRIMARY KEY,
    id VARCHAR(20) NOT NULL,
    payment_id VARCHAR(20) NOT NULL,
    source VARCHAR(50) NOT NULL,
    current_state VARCHAR(50) NOT NULL,
    created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_date TIMESTAMP WITHOUT TIME ZONE NOT NULL
);
