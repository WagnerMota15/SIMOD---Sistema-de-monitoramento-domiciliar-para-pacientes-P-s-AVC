CREATE TABLE patient (
    id UUID PRIMARY KEY,
    stroke_type VARCHAR(50),
    FOREIGN KEY (id) REFERENCES users(id_user) ON DELETE CASCADE
);