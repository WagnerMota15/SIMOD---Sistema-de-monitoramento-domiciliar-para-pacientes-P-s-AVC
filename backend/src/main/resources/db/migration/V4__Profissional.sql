CREATE TABLE professional(
    numCouncil VARCHAR(20) UNIQUE PRIMARY KEY,
    user_id UUID NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);