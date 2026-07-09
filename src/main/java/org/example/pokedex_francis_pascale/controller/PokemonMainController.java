package org.example.pokedex_francis_pascale.controller;

import javafx.scene.control.Label;
import javafx.application.Platform;
import org.example.pokedex_francis_pascale.exceptions.PokemonIntrouvableException;
import org.example.pokedex_francis_pascale.modele.Pokemon;
import org.example.pokedex_francis_pascale.modele.PokemonDAO;
import org.example.pokedex_francis_pascale.service.PokemonApiService;
import org.example.pokedex_francis_pascale.view.PokemonViewFX;
import javafx.scene.image.Image;

public class PokemonMainController {

    private final PokemonDAO dao = new PokemonDAO();
    private final PokemonApiService service = new PokemonApiService();
    private final PokemonViewFX view;

    private Pokemon pokemonTrouverListe = null;

    public PokemonMainController(PokemonViewFX view) {
        this.view = view;
        view.bouttonRecherche.setOnAction(e -> chargerDepuisApi());
        view.bouttonCapturer.setOnAction(e -> capturerPokemon());
        view.listePokemon.getSelectionModel().selectedItemProperty()
                .addListener((obs, ancien, nouveau) -> {
                    afficherPokemonDetails(nouveau);
                    if (nouveau != null) {
                        view.bouttonCapturer.setDisable(true);
                    }
                });
    }

    public void chargerDepuisApi() {
        String recherche = view.champRecherche.getText();
        view.messageErreur.setText("");
        pokemonTrouverListe = null;
        view.bouttonCapturer.setDisable(true);

        if (recherche == null || recherche.trim().isEmpty()) {
            view.messageErreur.setText("Veuillez entrer un ID ou un nom.");
            return;
        }


        new Thread(() -> {
            try {
                Pokemon pokemon = service.recuperer(recherche.trim());
                pokemonTrouverListe = pokemon;

                Platform.runLater(() -> {
                    afficherPokemonDetails(pokemon);
                    view.bouttonCapturer.setDisable(false);
                });

            } catch (PokemonIntrouvableException e) {
                Platform.runLater(() -> {
                    view.messageErreur.setText("Erreur 404: " + e.getMessage());
                    clearPokemonDetails();
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    view.messageErreur.setText("Pokémon introuvable ou erreur d'API : " + e.getMessage());
                    clearPokemonDetails();
                });
            }
        }).start();

    }
    public void capturerPokemon() {
        if (pokemonTrouverListe == null) {
            return;
        }
        try {
            dao.sauvegarder(pokemonTrouverListe);
            refreshList();
            view.listePokemon.getSelectionModel().select(pokemonTrouverListe);
            view.bouttonCapturer.setDisable(true);
            view.champRecherche.clear();
        } catch (Exception e) {
            view.messageErreur.setText("Pokémon introuvable ou erreur d'API : " + e.getMessage());
        }
    }

    public void afficherPokemonDetails(Pokemon pokemon) {
        if (pokemon == null) {
            clearPokemonDetails();
            return;
        }

        view.conteneurTypes.getChildren().clear();
        view.lblPokemonIdNom.setText("#" + pokemon.id + " " + pokemon.nom);

        Label premierType = new Label(pokemon.type.toUpperCase());
        premierType.getStyleClass().addAll("type-badge", "type-" + pokemon.type.toLowerCase().trim());
        view.conteneurTypes.getChildren().add(premierType);

        if (pokemon.type2 != null && !pokemon.type2.trim().isEmpty()) {
            Label deuxiemeType = new Label(pokemon.type2.toUpperCase());
            deuxiemeType.getStyleClass().addAll("type-badge", "type-" + pokemon.type2.toLowerCase().trim());
            view.conteneurTypes.getChildren().add(deuxiemeType);
            view.conteneurTypes.setVisible(true);
            view.conteneurTypes.setManaged(true);
        } else {
            view.conteneurTypes.getStyleClass().add("types-container-box");
        }

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
