--liquibase formatted sql
--changeset tkomarova:20220819000 change card_template table

ALTER TABLE card_template ADD COLUMN currency_type TEXT;

ALTER TABLE card_template ALTER COLUMN currency_type SET NOT NULL;

ALTER TABLE card_template DROP COLUMN name;

ALTER TABLE card_template ADD COLUMN type TEXT;

ALTER TABLE card_template ALTER COLUMN type SET NOT NULL;
