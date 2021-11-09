CREATE TABLE IF NOT EXISTS public.invoice_position
(
    id integer,
    invoice_id integer NOT NULL,
    price numeric NOT NULL,
    product_id integer NOT NULL,
    count integer NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS public.invoice_position
    ADD CONSTRAINT invoice_id_fk FOREIGN KEY (invoice_id)
    REFERENCES public.invoice (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS public.invoice_position
    ADD CONSTRAINT product_id_fk FOREIGN KEY (product_id)
    REFERENCES public.product (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;