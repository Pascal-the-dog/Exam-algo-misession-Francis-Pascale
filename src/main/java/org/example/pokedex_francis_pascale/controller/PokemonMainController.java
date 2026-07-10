package org.example.pokedex_francis_pascale.controller;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.example.pokedex_francis_pascale.exceptions.ApiConnexionException;
import org.example.pokedex_francis_pascale.exceptions.ApiErreurException;
import org.example.pokedex_francis_pascale.exceptions.ApiTimeoutException;
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
    private javafx.scene.media.MediaPlayer mediaPlayer;

    public PokemonMainController(PokemonViewFX view) {
        this.view = view;
        view.bouttonRecherche.setOnAction(e -> chargerDepuisApi());
        view.bouttonCapturer.setOnAction(e -> capturerPokemon());
        view.bouttonRelacher.setOnAction(e -> relacherPokemon());
        view.listePokemon.getSelectionModel().selectedItemProperty()
                .addListener((obs, ancien, nouveau) -> {
                    pokemonTrouverListe = nouveau;
                    afficherPokemonDetails(nouveau);
                    if (nouveau != null) {
                        view.bouttonCapturer.setDisable(true);
                        view.bouttonRelacher.setDisable(false);

                        jouerPokemonCri();
                    }
                });
    }

    private void jouerPokemonCri() {
        Pokemon actif = pokemonTrouverListe;
        if (actif == null) {
            actif = view.listePokemon.getSelectionModel().getSelectedItem();
        }

        if (actif != null && actif.cry_url != null && !actif.cry_url.isEmpty()) {
            try {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.dispose();
                }

                view.messageErreur.setText("");

                String audioUrl = actif.cry_url.trim();
                java.net.URL url = new java.net.URL(audioUrl);

                java.io.File tempFile = java.io.File.createTempFile("pokemon_cry_", ".mp3");
                tempFile.deleteOnExit();

                try (java.io.InputStream in = url.openStream();
                     java.io.FileOutputStream out = new java.io.FileOutputStream(tempFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }

                javafx.scene.media.Media audioFile = new javafx.scene.media.Media(tempFile.toURI().toString());
                mediaPlayer = new javafx.scene.media.MediaPlayer(audioFile);

                mediaPlayer.setOnReady(() -> mediaPlayer.play());

                mediaPlayer.setOnError(() -> {
                    view.messageErreur.setText("Erreur Audio: " + mediaPlayer.getError().getMessage());
                });

            } catch (Exception ex) {
                view.messageErreur.setText("Impossible de charger le cri: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    public void chargerDepuisApi() {
        String recherche = view.champRecherche.getText();
        view.messageErreur.setText("");
        view.listePokemon.getSelectionModel().clearSelection();
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

                });

            } catch (PokemonIntrouvableException e) {
                Platform.runLater(() -> {
                    view.messageErreur.setText("Erreur 404 : " + e.getMessage());
                    clearPokemonDetails();
                });

            } catch (ApiErreurException e) {
                Platform.runLater(() -> {
                    view.messageErreur.setText("Erreur API : " + e.getMessage());
                    clearPokemonDetails();
                });

            } catch (ApiConnexionException e) {
                Platform.runLater(() -> {
                    view.messageErreur.setText("Erreur de connexion : " + e.getMessage());
                    clearPokemonDetails();
                });

            } catch (ApiTimeoutException e) {
                Platform.runLater(() -> {
                    view.messageErreur.setText("Erreur : " + e.getMessage());
                    clearPokemonDetails();
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    view.messageErreur.setText("Erreur inattendue : " + e.getMessage());
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

    public void relacherPokemon() {
        Pokemon selectionList = view.listePokemon.getSelectionModel().getSelectedItem();

        if (selectionList == null && pokemonTrouverListe != null) {
            for (Pokemon pokemonDansLaListe : view.listePokemon.getItems()) {
                if (pokemonDansLaListe.id == pokemonTrouverListe.id) {
                    selectionList = pokemonDansLaListe;
                    break;
                }
            }
        }
        if (selectionList == null) {
            return;
        }
        try{
            dao.relacherPokemon(selectionList.id);
            refreshList();
            view.bouttonRelacher.setDisable(true);
            pokemonTrouverListe = null;
            view.messageErreur.setText("");
        } catch (Exception e) {
            view.messageErreur.setText("Erreur lors du relachement : " + e.getMessage());
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


        boolean dejaCapturer = false;
        for (Pokemon pokemoncapturer : view.listePokemon.getItems()) {
            if (pokemoncapturer.id == pokemon.id) {
                dejaCapturer = true;
                break;
            }
        }
        if (dejaCapturer) {
            view.bouttonCapturer.setDisable(true);
            view.bouttonRelacher.setDisable(false);
        } else {
            view.bouttonCapturer.setDisable(false);
            view.bouttonRelacher.setDisable(true);
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

        view.bouttonRelacher.setDisable(true);

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
