package org.example.pokedex_francis_pascale.modele;

import org.example.pokedex_francis_pascale.utils.Capitalize;

import static org.example.pokedex_francis_pascale.utils.Capitalize.capitalize;

public class Pokemon {
    public int id;
    public String nom;
    public String type;
    public String type2;
    public int hp;
    public int attack;
    public int attackSp;
    public int defense;
    public int defenseSp;
    public int vitesse;
    public String image_url;
    public String cry_url;
    public Boolean favori;

    public Pokemon(){}

    @Override
    public String toString() {
        String fav = favori ? "*" : "";
        return fav + " " + capitalize(nom) + " · #" + id;
    }
}

