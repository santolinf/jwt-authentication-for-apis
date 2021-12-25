--
-- Authentication
-- Name: USER_ACCOUNT; Type: TABLE; Schema: simplysend; Owner: -
--
CREATE TABLE USER_ACCOUNT (
    ID bigint NOT NULL,
    USERNAME character varying(255) NOT NULL,
    PASSWORD character varying(255),
    ENABLED boolean,
    UNIQUE (USERNAME)
);

CREATE SEQUENCE USER_ACCOUNT_ID_SEQ
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
;

ALTER TABLE USER_ACCOUNT ALTER COLUMN id SET DEFAULT nextval('USER_ACCOUNT_ID_SEQ');

SELECT pg_catalog.setval('USER_ACCOUNT_ID_SEQ', 1, false);

ALTER TABLE ONLY USER_ACCOUNT
    ADD CONSTRAINT user_account_pkey PRIMARY KEY (ID);

--
-- Authentication
-- Name: USER_PROFILE; Type: TABLE; Schema: simplysend; Owner: -
--
CREATE TABLE USER_PROFILE (
    ID bigint NOT NULL,
    FIRST_NAME character varying(255),
    LAST_NAME character varying(255),
    EMAIL character varying(255) NOT NULL,
    AGE integer,
    PHONE character varying(255),
    ADDRESS character varying(255),
    TAG character varying(255),
    ROLE character varying(255),
    MANAGER_ID bigint,
    UNIQUE (EMAIL)
);

CREATE SEQUENCE USER_PROFILE_ID_SEQ
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
;

ALTER TABLE USER_PROFILE ALTER COLUMN id SET DEFAULT nextval('USER_PROFILE_ID_SEQ');

SELECT pg_catalog.setval('USER_PROFILE_ID_SEQ', 1, false);

ALTER TABLE ONLY USER_PROFILE
    ADD CONSTRAINT user_profile_pkey PRIMARY KEY (ID);
