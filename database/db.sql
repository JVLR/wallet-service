CREATE SCHEMA IF NOT EXISTS wallet;

CREATE TABLE IF NOT EXISTS wallet.users
(
    id
    VARCHAR
(
    255
) PRIMARY KEY UNIQUE,
    name VARCHAR
(
    150
),
    document VARCHAR
(
    14
) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE INDEX idx_users_document ON users (document);

CREATE TABLE IF NOT EXISTS wallet.wallets
(
    id
    VARCHAR
(
    255
) PRIMARY KEY UNIQUE,
    user_id VARCHAR
(
    255
) REFERENCES users
(
    id
) ON DELETE CASCADE,
    balance NUMERIC
(
    19,
    4
) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE INDEX idx_wallets_user_id ON wallets (user_id);

CREATE TABLE IF NOT EXISTS wallet.historic_balances
(
    id
    serial
    NOT
    NULL
    UNIQUE,
    wallet_id
    VARCHAR
(
    255
) REFERENCES wallets
(
    id
) ON DELETE CASCADE,
    amount NUMERIC
(
    19,
    4
) NOT NULL,
    operation VARCHAR
(
    50
) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE INDEX idx_historic_balances_wallet_id ON historic_balances (wallet_id);

COMMENT
ON COLUMN historic_balances.transaction_type IS 'DEPOSIT, WITHDRAW, TRANSFER';
