CREATE TABLE IF NOT EXISTS ACCOUNTS (
    ACCOUNT_ID VARCHAR(20) PRIMARY KEY,
    BALANCE NUMERIC(10,2)
);

INSERT INTO ACCOUNTS (ACCOUNT_ID, BALANCE) VALUES
('12345678', 1000000.00),
('88888888', 1000000.00);