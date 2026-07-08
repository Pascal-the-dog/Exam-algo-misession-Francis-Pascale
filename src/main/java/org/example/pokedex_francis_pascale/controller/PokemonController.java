package org.example.pokedex_francis_pascale.controller;

import org.example.pokedex_francis_pascale.modele.Pokemon;
import org.example.pokedex_francis_pascale.modele.PokemonDAO;
import org.example.pokedex_francis_pascale.service.PokemonApiService;
import org.example.pokedex_francis_pascale.view.PokemonViewFX;
import javafx.scene.image.Image;

public class PokemonMainController {

    private final PokemonDAO dao = new PokemonDAO();
    private final PokemonApiService service = new PokemonApiService();
    private final PokemonViewFx view;

    public PokemonMainController(PokemonViewFx view) {
        this.view = view;
        view.btnCharger.setOnAction(e -> chargerDeputisApi());
        view.listePokemon.getSelectionModel().selectedItemProperty()
                .addListener(());
    }

    public void chargerDeputisApi() {
        String uuid = view.champUuid.getText();
        try {
            Pokemon pokemon = service.recuperer.getText();
            dao.sauvegarder(pokemon);
            refreshList();
        } catch (Exception e) {
            view.messageErreur.setText("Pokemon Introuvable ou erreur d'API : " + e.getMessage());
        }
    }

    public void afficherPokemonDetails(Pokemon pokemon) {
        if (pokemon == null) {
            view.detail.clear();
            view.imagePokemon.setImage(null);
            return;
        }
        String Message =
               "Id : ( " + pokemon.id + ") " + pokemon.nom;
        if(pokemon.image_url !=null) {
            Image img = new Image(pokemon.image_url, true);
            view.imagePokemon.setImage(img);
        }
    }

    public void refreshList() {
        try {
            view.listePokemon.getItems().setAll(dao.lister());
        } catch (Exception e) {
            view.massageErreur.setText("Erreur dans la base de donnees" + e.getMessage());
        }
    }
    public void demarrer(){
        refreshList();
    }
}
