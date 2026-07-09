module org.example.pokedex_francis_pascale {
    requires javafx.controls;
    requires java.net.http;


    requires com.fasterxml.jackson.databind;
    requires java.sql;


    exports org.example.pokedex_francis_pascale;


    exports org.example.pokedex_francis_pascale.service;
    exports org.example.pokedex_francis_pascale.modele;


    opens org.example.pokedex_francis_pascale.modele to com.fasterxml.jackson.databind;
}
