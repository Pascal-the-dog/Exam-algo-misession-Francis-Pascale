module org.example.pokedex_francis_pascale {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.pokedex_francis_pascale to javafx.fxml;
    exports org.example.pokedex_francis_pascale;
}