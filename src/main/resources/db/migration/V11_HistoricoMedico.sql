CREATE EXTENSION IF NOT EXISTS "pgcrypto"

CREATE TABLE historical(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    historical VARCHAR(500) NOT NULL,
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE,
);