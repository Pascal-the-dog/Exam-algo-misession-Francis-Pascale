package org.example.pokedex_francis_pascale;
import org.example.pokedex_francis_pascale.controller.PokemonMainController;
import org.example.pokedex_francis_pascale.modele.Pokemon;
import org.example.pokedex_francis_pascale.view.PokemonView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.pokedex_francis_pascale.view.PokemonViewFX;

public class MainFX extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        PokemonViewFX view = new PokemonViewFX();

//        PokemonController ctrl = new PokemonController(view);
//        ctrl.demarrer();

        Scene scene = new Scene(view.getRoot(), 900, 500);
        scene.getStylesheets().add(
                getClass().getResource("style.css").toExternalForm()
        );
        stage.setTitle("Mes Pokemons");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


