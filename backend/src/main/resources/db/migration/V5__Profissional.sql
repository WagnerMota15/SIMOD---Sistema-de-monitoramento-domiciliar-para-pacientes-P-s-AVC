CREATE TABLE professional(
    id UUID PRIMARY KEY,
    num_council VARCHAR(20) NOT NULL UNIQUE,
    FOREIGN KEY (id) REFERENCES users(id_user) ON DELETE CASCADE
);