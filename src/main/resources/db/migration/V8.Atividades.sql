CREATE EXTENSION IF NOT EXISTS "pgcrypto"

CREATE TABLE activities(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(45) NOT NULL,
    description VARCHAR(100) NOT NULL,
    typeExercise VARCHAR(45) NOT NULL,
    frenqRecommended INTEGER NOT NULL,
    video_url VARCHAR(45),
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE,
    FOREIGN KEY (professional_numCouncil) REFERENCES professional(numCouncil) ON DELETE CASCADE
);