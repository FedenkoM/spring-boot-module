
CREATE TABLE IF NOT EXISTS verification_token (
    id BIGINT NOT NULL,
    expiration_time TIMESTAMP NULL,
    "token" VARCHAR(255) NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT verification_token_pkey PRIMARY KEY (id)
);

ALTER TABLE verification_token
ADD CONSTRAINT fk_verification_token_user
FOREIGN KEY (user_id)
REFERENCES users (id);
