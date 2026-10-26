-- Script de création de la base bcphoenix (MySQL)
-- L'application est en ddl-auto=validate : elle vérifie les tables mais ne les crée pas.

CREATE DATABASE IF NOT EXISTS bcphoenix;
USE bcphoenix;

CREATE TABLE IF NOT EXISTS roles (
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    last_name  VARCHAR(100) NOT NULL,
    first_name VARCHAR(50)  NOT NULL,
    role_id    INT          NOT NULL,
    email      VARCHAR(100),
    CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles (id)
);
