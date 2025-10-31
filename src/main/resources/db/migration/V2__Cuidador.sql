CREATE TABLE caregiver(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    possumPatient BOOLEAN NOT NULL,
    user_id UUID NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);