CREATE TABLE diet(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    freqMeal INTEGER NOT NULL,
    schedules VARCHAR(45) NOT NULL,
    description VARCHAR(100) NOT NULL,
    patient_id UUID NOT NULL,
    professional_numCouncil VARCHAR(20) UNIQUE NOT NULL,
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE,
    FOREIGN KEY (professional_numCouncil) REFERENCES professional(numCouncil) ON DELETE CASCADE
);