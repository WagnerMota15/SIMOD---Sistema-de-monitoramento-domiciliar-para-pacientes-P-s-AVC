CREATE TABLE report(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    expedience TIMESTAMP NOT NULL,
    assessment VARCHAR(200) NOT NULL,
    feedbackPatient VARCHAR(45),
    patient_id UUID NOT NULL,
    professional_numCouncil VARCHAR(20) UNIQUE NOT NULL,
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE,
    FOREIGN KEY (professional_numCouncil) REFERENCES professional(numCouncil) ON DELETE CASCADE
);