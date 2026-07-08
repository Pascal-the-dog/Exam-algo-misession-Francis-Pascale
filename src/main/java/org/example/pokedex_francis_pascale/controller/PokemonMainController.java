package org.example.pokedex_francis_pascale.controller;

import org.example.pokedex_francis_pascale.modele.Pokemon;
import org.example.pokedex_francis_pascale.modele.PokemonDAO;
import org.example.pokedex_francis_pascale.service.PokemonApiService;
import javafx.scene.image.Image;
import org.example.pokedex_francis_pascale.view.PokemonViewFX;

public class PokemonMainController {

    private final PokemonDAO dao = new PokemonDAO();
    private final PokemonApiService service = new PokemonApiService();
    private final PokemonViewFX view;

    public PokemonMainController(PokemonViewFX view) {
        this.view = view;
        view.btnCharger.setOnAction(e -> chargerDepuisApi());
        view.listePokemon.getSelectionModel().selectedItemProperty()
                .addListener((obs, ancien, nouveau) -> afficherPokemonDetails(nouveau) );

    }

    public void chargerDepuisApi() {
        String id = view.champId.getText();
        try {
            Pokemon pokemon = service.recuperer(id);
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

        StringBuilder sb = new StringBuilder();
        sb.append("ID : ").append(pokemon.id).append("\n");
        sb.append("Nom : ").append(pokemon.nom).append("\n");
        sb.append("Type : ").append(pokemon.type);
        if (pokemon.type2 != null) {
            sb.append(" / ").append(pokemon.type2);
        }
        sb.append("\n\n");

        sb.append("HP : ").append(pokemon.hp).append("\n");
        sb.append("Attaque : ").append(pokemon.attack).append("\n");
        sb.append("Défense : ").append(pokemon.defense).append("\n");
        sb.append("Attaque Spéciale : ").append(pokemon.attackSp).append("\n");
        sb.append("Défense Spéciale : ").append(pokemon.defenseSp).append("\n");
        sb.append("Vitesse : ").append(pokemon.vitesse).append("\n");

        view.detail.setText(sb.toString());

        if (pokemon.image_url != null) {
            Image img = new Image(pokemon.image_url, true);
            view.imagePokemon.setImage(img);
        }
    }


    public void refreshList() {
        try {
            view.listePokemon.getItems().setAll(dao.lister());
        } catch (Exception e) {
            view.messageErreur.setText("Erreur dans la base de donnees" + e.getMessage());
        }
    }
    public void demarrer(){
        refreshList();
    }
}
