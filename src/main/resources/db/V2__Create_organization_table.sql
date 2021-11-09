CREATE TABLE IF NOT EXISTS public.organization
(
    id integer,
    name character varying(64) NOT NULL,
    inn bigint NOT NULL,
    payment_account bigint NOT NULL,
    PRIMARY KEY (id)
);