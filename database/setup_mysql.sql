-- Run once as a MySQL administrator (for example, root).
-- Flyway creates the tables when the Spring Boot application starts.

CREATE DATABASE IF NOT EXISTS ats
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'ats'@'localhost' IDENTIFIED BY 'ats';
ALTER USER 'ats'@'localhost' IDENTIFIED BY 'ats';

GRANT ALL PRIVILEGES ON ats.* TO 'ats'@'localhost';
FLUSH PRIVILEGES;
