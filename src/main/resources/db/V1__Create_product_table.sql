CREATE TABLE IF NOT EXISTS public.product
(
    id integer,
    name character varying(64) NOT NULL,
    internal_code bigint NOT NULL,
    PRIMARY KEY (id)
);