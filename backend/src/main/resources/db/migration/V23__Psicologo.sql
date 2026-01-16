CREATE TABLE psychologist (
    id UUID PRIMARY KEY REFERENCES professional(id)
);