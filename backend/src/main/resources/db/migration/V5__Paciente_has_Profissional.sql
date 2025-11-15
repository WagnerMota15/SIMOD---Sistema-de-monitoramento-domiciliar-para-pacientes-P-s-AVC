CREATE TABLE professionalHasPatient(
    professional_numCouncil VARCHAR(20) UNIQUE NOT NULL,
    patient_id UUID NOT NULL,
    PRIMARY KEY (professional_numCouncil, patient_id),
    FOREIGN KEY (professional_numCouncil) REFERENCES professional(numCouncil) ON DELETE CASCADE,
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE
);