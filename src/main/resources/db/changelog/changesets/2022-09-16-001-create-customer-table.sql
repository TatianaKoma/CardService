--liquibase formatted sql
--changeset tkomarova:20220916001 create customer table

CREATE TYPE role_title AS ENUM (' ROLE_ADMIN', ' ROLE_USER');

CREATE TABLE customer
(
    customer_id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    first_name   VARCHAR(45)        NOT NULL,
    last_name    VARCHAR(45)        NOT NULL,
    email       VARCHAR(60) UNIQUE NOT NULL,
    role        role_title         NOT NULL
);

ALTER TABLE customer DROP COLUMN role;
DROP TYPE role_title;

ALTER TABLE customer ADD COLUMN created_at TIMESTAMP WITH TIME ZONE;
ALTER TABLE customer ALTER COLUMN created_at SET NOT NULL;

ALTER TABLE card
    ADD COLUMN customer_id INT,
    ADD FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON UPDATE CASCADE ON DELETE SET NULL;
