CREATE TABLE address_has_patient (
    patient_id UUID NOT NULL,
    address_id UUID NOT NULL,
    principal BOOLEAN NOT NULL,
    PRIMARY KEY (patient_id, address_id),
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE,
    FOREIGN KEY (address_id) REFERENCES address(id) ON DELETE CASCADE
);
