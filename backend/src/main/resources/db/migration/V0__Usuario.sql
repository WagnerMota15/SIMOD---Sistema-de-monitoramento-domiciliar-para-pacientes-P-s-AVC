CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE users(
    id_user UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    name_complete VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    telephone VARCHAR(20),
    user_type VARCHAR(31) NOT NULL,
    role VARCHAR(20) NOT NULL
);