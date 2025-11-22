CREATE TABLE historical(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    historical VARCHAR(500) NOT NULL,
    patient_id UUID NOT NULL,
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE
);