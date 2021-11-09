CREATE TABLE IF NOT EXISTS public.invoice
(
    id integer,
    date date NOT NULL,
    organization_id integer NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS public.invoice
    ADD CONSTRAINT organization_id_fk FOREIGN KEY (organization_id)
    REFERENCES public.organization (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;