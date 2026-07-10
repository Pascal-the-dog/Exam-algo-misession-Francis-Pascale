package org.example.pokedex_francis_pascale.exceptions;

public class ApiTimeoutException extends RuntimeException {
    public ApiTimeoutException() {
        super("La requête a expiré");
    }
}
