-- Table: public.pokemon

-- DROP TABLE IF EXISTS public.pokemon;

CREATE TABLE IF NOT EXISTS public.pokemon
(
    id integer NOT NULL,
    nom character varying(50) COLLATE pg_catalog."default" NOT NULL,
    type character varying(100) COLLATE pg_catalog."default",
    type_2 character varying(100) COLLATE pg_catalog."default",
    hp integer,
    attaque integer,
    defense integer,
    attaque_speciale integer,
    defense_speciale integer,
    vitesse integer,
    image_url text COLLATE pg_catalog."default",
    capture_le timestamp without time zone DEFAULT now(),
    cry_url character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT pokemon_pkey PRIMARY KEY (id)
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.pokemon
    OWNER to postgres;