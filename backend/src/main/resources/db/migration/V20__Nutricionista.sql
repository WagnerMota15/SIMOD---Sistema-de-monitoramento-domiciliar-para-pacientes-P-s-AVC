CREATE TABLE nutritionist (
    id UUID PRIMARY KEY REFERENCES professional(id)
);