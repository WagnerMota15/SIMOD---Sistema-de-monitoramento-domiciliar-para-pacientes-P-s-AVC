CREATE TABLE address (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    cep VARCHAR(10) NOT NULL,
    street VARCHAR(255),
    neighborhood VARCHAR(255),
    city VARCHAR(255),
    state VARCHAR(2),
    number VARCHAR(10)
);
