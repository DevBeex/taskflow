CREATE TABLE role(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE status (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(50) NOT NULL UNIQUE,
                          description VARCHAR(255),
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          updated_at TIMESTAMP
);

CREATE TABLE active_sessions (
                                 id BIGSERIAL PRIMARY KEY,
                                 device_info VARCHAR(100) NOT NULL,
                                 token VARCHAR(500) NOT NULL,
                                 last_active TIMESTAMP NOT NULL,
                                 user_id BIGINT NOT NULL,
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                 updated_at TIMESTAMP,
                                 FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

ALTER TABLE users
    ADD COLUMN role_id BIGINT NOT NULL,
    ADD COLUMN status_id BIGINT NOT NULL,
    ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    ADD COLUMN updated_at TIMESTAMP,
    ADD CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES role(id),
    ADD CONSTRAINT fk_user_status FOREIGN KEY (status_id) REFERENCES status(id);

