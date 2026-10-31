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
    email      VARCHAR(100) NOT NULL UNIQUE,
    password_hash varchar(255) not null,
    CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles (id)
);
CREATE TABLE IF NOT EXISTS affiliates (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    last_name      VARCHAR(100) NOT NULL,
    first_name     VARCHAR(50)  NOT NULL,
    license_number VARCHAR(30)  NOT NULL UNIQUE,  -- clé utilisée à l'inscription
    birth_date     DATE,                          -- servira de 2e vérification à l'inscription
    active         BOOLEAN      NOT NULL DEFAULT TRUE,
    user_id        INT          UNIQUE,
    CONSTRAINT fk_affiliates_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE SET NULL
);

-- Les bowlings où le club joue
CREATE TABLE IF NOT EXISTS locations (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(100) NOT NULL UNIQUE,
    city    VARCHAR(100),
    address VARCHAR(255)
);

-- Un entraînement / une compétition à une date et un lieu donnés
CREATE TABLE IF NOT EXISTS events (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    event_date  DATE         NOT NULL,
    type        VARCHAR(20)  NOT NULL,   -- TRAINING, COMPETITION, LEAGUE
    location_id INT          NOT NULL,
    notes       VARCHAR(255),
    CONSTRAINT fk_events_location FOREIGN KEY (location_id) REFERENCES locations (id)
);

-- Une ligne = une partie d'un affilié lors d'un événement
CREATE TABLE IF NOT EXISTS games (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    event_id     INT      NOT NULL,
    affiliate_id INT      NOT NULL,
    game_number  INT  NOT NULL,
    score        INT NOT NULL,
    created_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_games_event     FOREIGN KEY (event_id)     REFERENCES events (id),
    CONSTRAINT fk_games_affiliate FOREIGN KEY (affiliate_id) REFERENCES affiliates (id),
    CONSTRAINT uq_games UNIQUE (event_id, affiliate_id, game_number),
    CONSTRAINT chk_games_score CHECK (score BETWEEN 0 AND 300)
);