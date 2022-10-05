-- postgres db schema
SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET client_min_messages = WARNING;

CREATE SCHEMA IF NOT EXISTS test_data_storage;

SET search_path = test_data_storage, pg_catalog, public;

CREATE TABLE test_data_storage.omni_queue
(
    id        INTEGER NOT NULL PRIMARY KEY,
    data_type TEXT,
    data      TEXT,
    archived  BOOLEAN,
    created   TIMESTAMP WITHOUT TIME ZONE,
    updated   TIMESTAMP WITHOUT TIME ZONE
);

CREATE INDEX idx_omni_queue_id ON test_data_storage.omni_queue (id);
CREATE INDEX idx_omni_queue_data_type ON test_data_storage.omni_queue (data_type);
CREATE INDEX idx_omni_queue_archived ON test_data_storage.omni_queue (archived);
CREATE INDEX idx_omni_queue_updated ON test_data_storage.omni_queue (updated);
CREATE INDEX idx_omni_queue_created ON test_data_storage.omni_queue (created);

CREATE SEQUENCE test_data_storage.omni_queue_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

CREATE TABLE test_data_storage.shedlock
(
    name       VARCHAR(64)  NOT NULL PRIMARY KEY,
    lock_until TIMESTAMP(3) NULL,
    locked_at  TIMESTAMP(3) NULL,
    locked_by  VARCHAR
);

CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

CREATE TABLE test_data_storage.omni_type
(
    id        BIGINT PRIMARY KEY,
    data_type TEXT,
    meta      TEXT,
    created   TIMESTAMP WITHOUT TIME ZONE,
    updated   TIMESTAMP WITHOUT TIME ZONE
);

CREATE SEQUENCE test_data_storage.omni_type_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER TABLE test_data_storage.omni_type
    ADD CONSTRAINT constraint_type UNIQUE (data_type);

