CREATE TABLE sessions(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    dateTime TIMESTAMP NOT NULL,
    remote BOOLEAN NOT NULL,
    place VARCHAR(80),
    patient_id UUID NOT NULL,
    professional_id UUID UNIQUE NOT NULL,
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE,
    FOREIGN KEY (professional_id) REFERENCES professional(id) ON DELETE CASCADE
);