CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE sessions(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    dateTime TIMESTAMP NOT NULL,
    remote BOOLEAN NOT NULL,
    place VARCHAR(80),
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE,
    FOREIGN KEY (professional_numCouncil) REFERENCES professional(numCouncil) ON DELETE CASCADE
);