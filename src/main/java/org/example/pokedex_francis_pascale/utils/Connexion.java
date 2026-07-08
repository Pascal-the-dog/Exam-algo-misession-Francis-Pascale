package org.example.pokedex_francis_pascale.utils;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {
    private static final String URL = "jdbc:postgresql://localhost:5432/ghiblidb";
    private static final String USER = "postgres";
    private static final String PASS = "slcwsnbm";

    private Connexion(){};

    public static Connection getConnexion() throws SQLException {
        return DriverManager.getConnection(URL,USER,PASS);
    }
}