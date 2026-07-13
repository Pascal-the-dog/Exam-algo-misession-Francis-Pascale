package org.example.pokedex_francis_pascale.utils;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {
    private static final String URL = "";
    private static final String USER = "";
    private static final String PASS = "";

    private Connexion(){};

    public static Connection getConnexion() throws SQLException {
        return DriverManager.getConnection(URL,USER,PASS);
    }
}
