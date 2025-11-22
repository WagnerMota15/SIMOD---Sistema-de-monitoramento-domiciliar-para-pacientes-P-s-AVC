CREATE TABLE address(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    cep VARCHAR(15) NOT NULL,
    number VARCHAR(6) NOT NULL,
    description VARCHAR(45),
    patient_id UUID NOT NULL,
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE
);