CREATE TABLE activities(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(45) NOT NULL,
    description VARCHAR(100) NOT NULL,
    typeExercise VARCHAR(45) NOT NULL,
    freqRecommended INTEGER NOT NULL,
    video_url VARCHAR(45),
    patient_id UUID NOT NULL,
    professional_id UUID NOT NULL,
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE,
    FOREIGN KEY (professional_id) REFERENCES professional(id) ON DELETE CASCADE
);