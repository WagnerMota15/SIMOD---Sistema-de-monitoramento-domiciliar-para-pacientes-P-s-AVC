CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE family(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(80) NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    kinship VARCHAR(20) NOT NULL,
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE,
);