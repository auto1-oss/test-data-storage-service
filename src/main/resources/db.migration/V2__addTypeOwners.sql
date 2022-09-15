CREATE TABLE test_data_provider.type_owners
(
    id BIGINT PRIMARY KEY,
    data_type TEXT,
    owner VARCHAR
);

CREATE SEQUENCE test_data_provider.type_owners_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER TABLE test_data_provider.type_owners ADD CONSTRAINT constraint_type UNIQUE (data_type);