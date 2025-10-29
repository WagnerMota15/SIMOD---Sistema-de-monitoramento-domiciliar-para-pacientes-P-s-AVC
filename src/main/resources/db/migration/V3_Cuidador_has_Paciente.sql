CREATE EXTENSION IF NOT EXISTS "pgcrypto"

CREATE TABLE caregiverHasPatient(
    caregiver_id INT,
    patient_id INT,
    PRIMARY KEY (caregiver_id, patient_id),
    FOREIGN KEY (caregiver_id) REFERENCES caregiver(id) ON DELETE CASCADE,
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE,
);