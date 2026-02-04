CREATE TABLE libraries (
    id bigserial PRIMARY KEY,
    name varchar(255) NOT NULL,
    version varchar(50) NOT NULL,
    source varchar(50) NOT NULL,
    license_name varchar(100),
    health_score integer,
    last_release varchar(50),
    repository varchar(500),
    description text,
    created_at timestamp NOT NULL,
    updated_at timestamp
);

CREATE INDEX idx_library_name ON libraries(name);
CREATE INDEX idx_library_source ON libraries(source);
CREATE INDEX idx_library_health_score ON libraries(health_score);
