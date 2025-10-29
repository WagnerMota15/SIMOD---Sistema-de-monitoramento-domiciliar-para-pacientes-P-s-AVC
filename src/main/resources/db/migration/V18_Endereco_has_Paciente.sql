CREATE EXTENSION IF NOT EXISTS "pgcrypto"

CREATE TABLE addressHasPatient(
    address_id INT,
    patient_id INT,
    PRIMARY KEY (address_id, patient_id),
    FOREIGN KEY (address_id) REFERENCES address(id) ON DELETE CASCADE,
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE,
);