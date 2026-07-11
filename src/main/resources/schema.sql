DROP TABLE IF EXISTS public.pokemon;

CREATE TABLE public.pokemon (
                                id INTEGER NOT NULL,
                                nom VARCHAR(50) NOT NULL,
                                type VARCHAR(100),
                                type_2 VARCHAR(100),
                                hp INTEGER,
                                attaque INTEGER,
                                defense INTEGER,
                                attaque_speciale INTEGER,
                                defense_speciale INTEGER,
                                vitesse INTEGER,
                                image_url TEXT,
                                capture_le TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
                                cry_url VARCHAR(255),
                                favori BOOLEAN DEFAULT FALSE,
                                CONSTRAINT pokemon_pkey PRIMARY KEY (id)
);
