-- postgres db schema
SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = ON;
SET check_function_bodies = FALSE;
SET client_min_messages = WARNING;

CREATE SCHEMA IF NOT EXISTS test_data_provider;

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;
SET search_path = test_data_provider, pg_catalog, public;

SET default_tablespace = '';

SET default_with_oids = FALSE;

CREATE TABLE test_data_provider.omni_queue
(
    id INTEGER NOT NULL PRIMARY KEY,
    data_type TEXT,
    data TEXT,
    dirty BOOLEAN,
    created TIMESTAMP WITHOUT TIME ZONE,
    updated TIMESTAMP WITHOUT TIME ZONE
);

CREATE SEQUENCE test_data_provider.omni_queue_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

CREATE TABLE test_data_provider.shedlock
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

