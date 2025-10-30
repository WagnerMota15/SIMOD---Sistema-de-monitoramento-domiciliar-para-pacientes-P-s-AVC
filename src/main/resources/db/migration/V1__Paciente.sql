CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE patient (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    typeAVC VARCHAR(45) NOT NULL,
    possumProfessional BOOLEAN NOT NULL,
    possumCaregiver BOOLEAN NOT NULL,
    user_id UUID NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);