CREATE TABLE vehicles (
                          id BIGSERIAL PRIMARY KEY,
                          number VARCHAR(255) NOT NULL UNIQUE,
                          type VARCHAR(20) NOT NULL,
                          status VARCHAR(20) NOT NULL,
                          entry_time TIMESTAMP,
                          exit_time TIMESTAMP
);