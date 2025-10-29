CREATE EXTENSION IF NOT EXISTS "pgcrypto"

CREATE TABLE professionalHasPatient(
    professional_id INT,
    patient_id INT,
    PRIMARY KEY (professional_id, patient_id),
    FOREIGN KEY (professional_id) REFERENCES professional(id) ON DELETE CASCADE,
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE,
);