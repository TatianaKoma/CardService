--liquibase formatted sql
--changeset tkomarova:20221010000 change pin and cvv columns

ALTER TABLE card ALTER COLUMN pin TYPE VARCHAR(4);

ALTER TABLE card ALTER COLUMN cvv TYPE VARCHAR(3);
