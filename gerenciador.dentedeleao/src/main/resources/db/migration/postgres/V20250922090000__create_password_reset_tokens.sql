CREATE TABLE IF NOT EXISTS dente_de_leao_manager.password_reset_tokens (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    token VARCHAR(100) NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    used BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES dente_de_leao_manager.users(id)
);

CREATE INDEX idx_password_reset_token ON dente_de_leao_manager.password_reset_tokens(token);
