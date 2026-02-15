CREATE TABLE patient (
    id UUID PRIMARY KEY,
    stroke_type VARCHAR(50),
    address_id UUID,
    CONSTRAINT fk_patient_user
        FOREIGN KEY (id) REFERENCES users(id_user) ON DELETE CASCADE,
    CONSTRAINT fk_patient_address
        FOREIGN KEY (address_id) REFERENCES address(id)
);