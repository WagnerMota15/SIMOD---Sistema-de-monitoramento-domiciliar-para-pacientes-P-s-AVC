CREATE TABLE user_devices (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    fcm_token VARCHAR(255) NOT NULL UNIQUE,
    platform VARCHAR(20),
    created_at TIMESTAMP DEFAULT now(),
    last_login TIMESTAMP,
    CONSTRAINT fk_user_device_user
        FOREIGN KEY (user_id)
        REFERENCES users(id_user)
        ON DELETE CASCADE
);
