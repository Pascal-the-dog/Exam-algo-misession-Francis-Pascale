DROP TABLE IF EXISTS pokemon ;

    CREATE TABLE pokemon (
    id INT PRIMARY KEY,
    nom VARCHAR (50) NOT NULL,
    type VARCHAR (100),
    type_2 VARCHAR (100),
    hp INT,
    attaque INT,
    defense INT,
    attaque_speciale INT,
    defense_speciale INT,
    vitesse INT,
    image_url TEXT,
    capture_le TIMESTAMP DEFAULT NOW (),
);


-- DROP TABLE IF EXISTS public pokemon;
-- (
--     id integer NOT NULL,
--     nom character varying(50) COLLATE pg_catalog."default" NOT NULL,
--     type character varying(100) COLLATE pg_catalog."default",
--     type_2 character varying(100) COLLATE pg_catalog."default",
--     hp integer,
--     attaque integer,
--     defense integer,
--     attaque_speciale integer,
--     defense_speciale integer,
--     vitesse integer,
--     image_url text COLLATE pg_catalog."default",
--     capture_le timestamp without time zone DEFAULT now(),
--     cry_url character varying(255) COLLATE pg_catalog."default",
--     CONSTRAINT pokemon_pkey PRIMARY KEY (id)
--     )