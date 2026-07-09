package org.example.pokedex_francis_pascale.controller;

import org.example.pokedex_francis_pascale.modele.Pokemon;
import org.example.pokedex_francis_pascale.modele.PokemonDAO;
import org.example.pokedex_francis_pascale.service.PokemonApiService;
import org.example.pokedex_francis_pascale.view.PokemonViewFX;
import javafx.scene.image.Image;

public class PokemonMainController {

    private final PokemonDAO dao = new PokemonDAO();
    private final PokemonApiService service = new PokemonApiService();
    private final PokemonViewFX view;

    public PokemonMainController(PokemonViewFX view) {
        this.view = view;
        view.bouttonRecherche.setOnAction(e -> chargerDepuisApi());
        view.listePokemon.getSelectionModel().selectedItemProperty()
                .addListener((obs, ancien, nouveau) -> afficherPokemonDetails(nouveau));
    }

    public void chargerDepuisApi() {
        String recherche = view.champRecherche.getText();
        view.messageErreur.setText("");

        if (recherche == null || recherche.trim().isEmpty()) {
            view.messageErreur.setText("Veuillez entrer un ID ou un nom.");
            return;
        }

        try {
            Pokemon pokemon = service.recuperer(recherche.trim());
            dao.sauvegarder(pokemon);
            refreshList();

            view.listePokemon.getSelectionModel().select(pokemon);
        } catch (Exception e) {
            view.messageErreur.setText("Pokémon introuvable ou erreur d'API : " + e.getMessage());
        }
    }

    public void afficherPokemonDetails(Pokemon pokemon) {
        if (pokemon == null) {
            clearPokemonDetails();
            return;
        }

        StringBuilder titre = new StringBuilder();
        titre.append("#").append(pokemon.id).append(" ").append(pokemon.nom).append(" - ").append(pokemon.type);
        if (pokemon.type2 != null && !pokemon.type2.isEmpty()) {
            titre.append(" / ").append(pokemon.type2);
        }
        view.lblPokemonIdNom.setText(titre.toString());

        view.valleurHp.setText(String.valueOf(pokemon.hp));
        view.valleurAttack.setText(String.valueOf(pokemon.attack));
        view.valleurAttackSp.setText(String.valueOf(pokemon.attackSp));
        view.valleurDefense.setText(String.valueOf(pokemon.defense));
        view.valleurDefenseSp.setText(String.valueOf(pokemon.defenseSp));
        view.valleurVitesse.setText(String.valueOf(pokemon.vitesse));

        view.barHp.setProgress(pokemon.hp / 255.0);
        view.barAttack.setProgress(pokemon.attack / 255.0);
        view.barAttackSp.setProgress(pokemon.attackSp / 255.0);
        view.barDefense.setProgress(pokemon.defense / 255.0);
        view.barDefenseSp.setProgress(pokemon.defenseSp / 255.0);
        view.barVitesse.setProgress(pokemon.vitesse / 255.0);

        if (pokemon.image_url != null && !pokemon.image_url.isEmpty()) {
            Image img = new Image(pokemon.image_url, true);
            view.imagePokemon.setImage(img);
        } else {
            view.imagePokemon.setImage(null);
        }
    }

    private void clearPokemonDetails() {
        view.lblPokemonIdNom.setText("Aucun Pokémon Sélectionné");

        view.valleurHp.setText("-");
        view.valleurAttack.setText("-");
        view.valleurAttackSp.setText("-");
        view.valleurDefense.setText("-");
        view.valleurDefenseSp.setText("-");
        view.valleurVitesse.setText("-");

        view.barHp.setProgress(0.0);
        view.barAttack.setProgress(0.0);
        view.barAttackSp.setProgress(0.0);
        view.barDefense.setProgress(0.0);
        view.barDefenseSp.setProgress(0.0);
        view.barVitesse.setProgress(0.0);

        view.imagePokemon.setImage(null);
    }

    public void refreshList() {
        try {
            view.listePokemon.getItems().setAll(dao.lister());
        } catch (Exception e) {
            view.messageErreur.setText("Erreur dans la base de données : " + e.getMessage());
        }
    }

    public void demarrer() {
        refreshList();
    }
}
