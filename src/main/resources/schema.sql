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
    capture_le TIMESTAMP DEFAULT NOW ()
);