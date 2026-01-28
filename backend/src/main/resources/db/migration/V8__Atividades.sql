CREATE TABLE activities(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(45) NOT NULL,
    description VARCHAR(100) NOT NULL,
    type_exercise VARCHAR(45) NOT NULL,
    freq_recommended INTEGER NOT NULL,
    video_url VARCHAR(200) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    patient_id UUID NOT NULL,
    professional_id UUID NOT NULL,
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE,
    FOREIGN KEY (professional_id) REFERENCES professional(id) ON DELETE CASCADE
);