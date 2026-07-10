package org.example.pokedex_francis_pascale.exceptions;

public class ApiConnexionException extends RuntimeException{
    public ApiConnexionException(){
        super("Impossible de se connection a l'API");
    }
}
