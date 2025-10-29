CREATE EXTENSION IF NOT EXISTS "pgcrypto"

CREATE TABLE user(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    CPF VARCHAR(11) NOT NULL,
    nomeComplete VARCHAR(80) NOT NULL,
    email VARCHAR(80) NOT NULL,
    password VARCHAR(60) NOT NULL,
    telephone VARCHAR(20) NOT NULL
);