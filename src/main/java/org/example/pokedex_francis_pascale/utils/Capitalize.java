package org.example.pokedex_francis_pascale.utils;

public class Capitalize {
    public static String capitalize(String texte) {
        if (texte == null || texte.isEmpty()) {
            return texte;
        }
        return texte.substring(0, 1).toUpperCase() + texte.substring(1);
    }
}
