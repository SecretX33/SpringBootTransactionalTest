--liquibase formatted sql

--changeset content:2025-01-15-001-init
--comment: Initial schema setup for content database

-- Create a version table to track schema version
CREATE TABLE IF NOT EXISTS schema_version (
    version VARCHAR(50) PRIMARY KEY,
    applied_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    description TEXT
);

-- Insert initial version
INSERT INTO schema_version (version, description)
VALUES ('1.0.0', 'Initial schema setup');

-- Add your initial tables here
