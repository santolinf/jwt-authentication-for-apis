--
-- Items
-- Name: ITEM; Type: TABLE; Schema: simplysend; Owner: -
--
CREATE TABLE ITEM (
    ID bigint NOT NULL,
    TYPE character varying(255),
    NAME character varying(255) NOT NULL,
    DESCRIPTION character varying(255),
    PRICE integer NOT NULL
);

CREATE SEQUENCE ITEM_ID_SEQ
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
;

ALTER TABLE ITEM ALTER COLUMN id SET DEFAULT nextval('ITEM_ID_SEQ');

SELECT pg_catalog.setval('ITEM_ID_SEQ', 1, false);

ALTER TABLE ONLY ITEM
    ADD CONSTRAINT item_pkey PRIMARY KEY (ID);
