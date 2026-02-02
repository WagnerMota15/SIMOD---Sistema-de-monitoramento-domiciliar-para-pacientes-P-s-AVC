CREATE TABLE medical (
    id UUID PRIMARY KEY REFERENCES professional(id)
);