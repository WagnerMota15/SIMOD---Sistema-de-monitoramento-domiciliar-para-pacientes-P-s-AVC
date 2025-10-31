CREATE TABLE medicine(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(45) NOT NULL,
    dosage DECIMAL(5,2) NOT NULL,
    unity VARCHAR(10) NOT NULL,
    frequency INTEGER NOT NULL,
    description VARCHAR(100) NOT NULL,
    patient_id UUID NOT NULL,
    professional_numCouncil VARCHAR(20) UNIQUE NOT NULL,
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE,
    FOREIGN KEY (professional_numCouncil) REFERENCES professional(numCouncil) ON DELETE CASCADE
);