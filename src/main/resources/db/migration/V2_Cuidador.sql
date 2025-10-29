CREATE EXTENSION IF NOT EXISTS "pgcrypto"

CREATE TABLE caregiver(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    possumPatient BOOLEAN NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);