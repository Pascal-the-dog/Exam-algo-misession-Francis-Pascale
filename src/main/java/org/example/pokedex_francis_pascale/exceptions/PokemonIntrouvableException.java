package org.example.pokedex_francis_pascale.exceptions;

public class PokemonIntrouvableException extends RuntimeException {
    public PokemonIntrouvableException() {
        super("Pokémon introuvable");
    }
}
