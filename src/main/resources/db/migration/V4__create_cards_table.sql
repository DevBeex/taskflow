CREATE TABLE card (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    tasklist_id BIGINT NOT NULL,
    CONSTRAINT fk_tasklist FOREIGN KEY (tasklist_id) REFERENCES tasklist (id) ON DELETE CASCADE
);
