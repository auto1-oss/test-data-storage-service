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

-- Changelogs

CREATE TABLE public.changelog
(
    id          INTEGER NOT NULL PRIMARY KEY,
    action      TEXT,
    comment_id  INTEGER,
    created_by  BIGINT,
    created_on  TIMESTAMP WITHOUT TIME ZONE,
    field       TEXT,
    new_value   TEXT,
    object_id   UUID,
    object_type TEXT,
    old_value   TEXT,
    parent_id   UUID,
    parent_type TEXT
);

CREATE TABLE public.changelog_comments
(
    id         INTEGER NOT NULL PRIMARY KEY,
    comment    TEXT,
    created_by BIGINT,
    created_on TIMESTAMP WITHOUT TIME ZONE
);

CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

CREATE TABLE test_data_storage.type_owners
(
    id        BIGINT PRIMARY KEY,
    data_type TEXT,
    owner     VARCHAR
);

CREATE SEQUENCE test_data_storage.type_owners_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER TABLE test_data_storage.type_owners
    ADD CONSTRAINT constraint_type UNIQUE (data_type);
