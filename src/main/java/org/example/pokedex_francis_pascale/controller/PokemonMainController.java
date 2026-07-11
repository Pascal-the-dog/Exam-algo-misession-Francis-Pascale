package org.example.pokedex_francis_pascale.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.example.pokedex_francis_pascale.exceptions.*;
import org.example.pokedex_francis_pascale.modele.Pokemon;
import org.example.pokedex_francis_pascale.modele.PokemonDAO;
import org.example.pokedex_francis_pascale.service.PokemonApiService;
import org.example.pokedex_francis_pascale.utils.ConfirmationBox;
import org.example.pokedex_francis_pascale.utils.MessageUtils;
import org.example.pokedex_francis_pascale.view.PokemonViewFX;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PokemonMainController {

    private final PokemonDAO dao = new PokemonDAO();
    private final PokemonApiService service = new PokemonApiService();
    private final PokemonViewFX view;
    private Pokemon pokemonTrouverListe = null;
    private javafx.scene.media.MediaPlayer mediaPlayer;
    private List<Pokemon> pokemonListe = new ArrayList<>();
    private Pokemon pokemonActuel;

    public PokemonMainController(PokemonViewFX view) {

        this.view = view;
        view.bouttonRecherche.setOnAction(e -> gererRecherche());
        view.bouttonCapturer.setOnAction(e -> capturerPokemon());
        view.bouttonRelacher.setOnAction(e -> relacherPokemon());
        view.bouttonFavori.setOnAction(e -> toggleFavori());
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
        view.selecteurOption.setOnAction(this::onOptionChange);
        view.bouttonFavori.setOnAction(e -> toggleFavori());
    }


    private void onOptionChange(ActionEvent e) {
        String valeur = view.selecteurOption.getValue();
        switch (valeur) {
            case "Type" -> {
                view.montrerCombo();
                view.listePokemon.getItems().setAll(pokemonListe);
                view.comboRecherche.getSelectionModel().clearSelection();
                view.comboRecherche.setValue(null);
                construireCombo(true);
                view.comboRecherche.setOnAction(choix -> gererRecherche());
            }
            case "Génération" -> {
                view.montrerCombo();
                view.listePokemon.getItems().setAll(pokemonListe);  // reaffiche la liste du pokedex
                view.comboRecherche.getSelectionModel().clearSelection();  // clear les valeurs de la combo
                view.comboRecherche.setValue(null);
                construireCombo(false);
                view.comboRecherche.setOnAction(choix -> gererRecherche());
            }
            case "ID / Nom" -> {
                view.montrerChampTexte();
                view.comboRecherche.getSelectionModel().clearSelection();
                view.comboRecherche.setValue(null);
                view.listePokemon.getItems().setAll(pokemonListe);
                view.comboRecherche.setOnAction(choix -> gererRecherche());
            }
            case "Favoris" -> {
                view.champRecherche.setVisible(false);
                view.champRecherche.setManaged(true);
                view.comboRecherche.setVisible(false);
                view.comboRecherche.setManaged(false);
                filtrerFavoris();
            }



            }
    }

    public void construireCombo(boolean type){
        if (type){
            List<String> types = pokemonListe.stream()
                    .flatMap(p -> {
                        List<String> t = new ArrayList<>();
                        if (p.type !=null) t.add(p.type.toLowerCase());
                        if (p.type2 != null) t.add(p.type2.toLowerCase());
                        return t.stream();
                    })
                    .distinct()
                    .sorted()
                    .toList();
            view.comboRecherche.getItems().setAll(types);

        } else {
            view.comboRecherche.getItems().setAll(List.of("1","2","3","4","5","6","7","8","9"));
        }

    }


    private void jouerPokemonCri() {
        Pokemon actif = pokemonTrouverListe;
        if (actif == null) {
            actif = view.listePokemon.getSelectionModel().getSelectedItem();
        }

        if (actif == null || actif.cry_url == null || actif.cry_url.trim().isEmpty()) {
            Platform.runLater(() -> {
                MessageUtils.afficherMessage(view.messageErreur, "Ce Pokémon n'a pas de cri disponible.");
            });
            return;
        }

        final String audioUrl = actif.cry_url.trim();

        new Thread(() -> {
            try {
                if (mediaPlayer != null) {
                    try {
                        MediaPlayer.Status status = mediaPlayer.getStatus();
                        if (status == MediaPlayer.Status.READY ||
                                status == MediaPlayer.Status.PLAYING ||
                                status == MediaPlayer.Status.PAUSED ||
                                status == MediaPlayer.Status.STOPPED) {
                            mediaPlayer.stop();
                        }
                    } catch (Exception ignored) {
                    }
                    mediaPlayer.dispose();
                }

                Platform.runLater(() -> MessageUtils.effacerMessage(view.messageErreur));

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
                if (tempFile.length() == 0) {
                    Platform.runLater(() -> {
                        MessageUtils.afficherMessage(view.messageErreur, "Ce Pokémon n'a pas de cri disponible.");
                    });
                    return;
                }

                Platform.runLater(() -> {
                    try {
                        Media audioFile = new Media(tempFile.toURI().toString());
                        mediaPlayer = new MediaPlayer(audioFile);
                        mediaPlayer.setVolume(0.2);



                        mediaPlayer.setOnReady(() -> mediaPlayer.play());

                        mediaPlayer.setOnError(() -> {
                            Throwable err = mediaPlayer.getError();
                            String msg = (err != null ? err.getMessage() : "Erreur audio inconnue");
                            MessageUtils.afficherMessage(view.messageErreur, "Erreur Audio: " + msg);
                        });
                    } catch (Exception e) {
                        MessageUtils.afficherMessage(view.messageErreur, "Erreur Audio: " + e.getMessage());
                    }
                });

            } catch (Exception ex) {
                Platform.runLater(() -> {
                    MessageUtils.afficherMessage(view.messageErreur, "Impossible de charger le cri: " + ex.getMessage());
                });
                ex.printStackTrace();
            }
        }).start();
    }


    public void gererRecherche() {
        String option = view.selecteurOption.getValue();

        MessageUtils.effacerMessage(view.messageErreur);;

        view.bouttonCapturer.setDisable(true);

        String recherche;

        if ("Type".equals(option) || "Génération".equals(option)) {
            recherche = view.comboRecherche.getValue();
        } else {
            recherche = view.champRecherche.getText();
        }

        if (recherche == null || recherche.trim().isEmpty()) {
            clearPokemonDetails();
            view.bouttonRelacher.setDisable(true);
            return;
        }

        if ("Favoris".equals(option)) {
            filtrerFavoris();
            return;
        }

        if ("Type".equals(option)) {
            filtrerInventaireParType(view.comboRecherche.getValue());
        } else if ("Génération".equals(option)) {
            filtrerInventaireParGeneration(recherche.trim());
        } else {
            chargerDepuisApi();
        }


    }

    private void filtrerFavoris() {
        List<Pokemon> favoris = pokemonListe.stream()
                .filter(p -> p.favori)
                .toList();

        view.listePokemon.getItems().setAll(favoris);

        clearPokemonDetails();
        view.bouttonRelacher.setDisable(true);
        view.bouttonFavori.setDisable(true);

        if (favoris.isEmpty()) {
            MessageUtils.afficherMessage(view.messageErreur, "Aucun Pokémon favori.");
        } else {
            MessageUtils.effacerMessage(view.messageErreur);
        }
    }



    private void filtrerInventaireParGeneration(String texteGen) {
        int gen;
        try {
            gen = Integer.parseInt(view.comboRecherche.getValue());
        } catch (NumberFormatException e) {
            MessageUtils.afficherMessage(view.messageErreur, "Veuillez entrer un nombre valide (ex: 1 pour Gen 1).");
            return;
        }
        int minId = 0;
        int maxId = 0;

        switch (gen) {
            case 1 -> { minId = 1;    maxId = 151;  }
            case 2 -> { minId = 152;  maxId = 251;  }
            case 3 -> { minId = 252;  maxId = 386;  }
            case 4 -> { minId = 387;  maxId = 493;  }
            case 5 -> { minId = 494;  maxId = 649;  }
            case 6 -> { minId = 650;  maxId = 721;  }
            case 7 -> { minId = 722;  maxId = 809;  }
            case 8 -> { minId = 810;  maxId = 905;  }
            case 9 -> { minId = 906;  maxId = 1025; }
            default -> {
                MessageUtils.afficherMessage(view.messageErreur, "Génération " + gen + " non supportée (Choisissez de 1 à 9).");
                return;
            }
        }

        try {
            List<Pokemon> tousLesPokemon = dao.lister();
            final int startRange = minId;
            final int endRange = maxId;
            List<Pokemon> filtrer = tousLesPokemon.stream()
                    .filter(p -> p.id >= startRange && p.id <= endRange)
                    .collect(Collectors.toList());

            if (filtrer.isEmpty()) {
                MessageUtils.afficherMessage(view.messageErreur, "Aucun Pokémon de la Génération " + gen + " dans votre inventaire.");
            }

            view.listePokemon.getItems().setAll(filtrer);
            clearPokemonDetails();
            view.bouttonRelacher.setDisable(true);

        } catch (Exception e) {
            MessageUtils.afficherMessage(view.messageErreur, "Erreur lors du filtrage par génération : " + e.getMessage());
        }
    }

    private void filtrerInventaireParType(String typeRecherche) {
        try {
            List<Pokemon> tousLesPokemon = dao.lister();

            List<Pokemon> filtrer = tousLesPokemon.stream()
                    .filter(p -> (p.type != null && p.type.toLowerCase().contains(typeRecherche)) ||
                            (p.type2 != null && p.type2.toLowerCase().contains(typeRecherche)))
                    .collect(Collectors.toList());

            if (filtrer.isEmpty()) {
                MessageUtils.afficherMessage(view.messageErreur, "Aucun Pokémon de type '" + typeRecherche + "' dans votre inventaire.");
            }
            view.listePokemon.getItems().setAll(filtrer);
            clearPokemonDetails();
            view.bouttonRelacher.setDisable(true);

        } catch (Exception e) {
            MessageUtils.afficherMessage(view.messageErreur, "Erreur lors du filtrage par type : " + e.getMessage());
        }
    }

    public void chargerDepuisApi() {
        String recherche = view.champRecherche.getText();
        MessageUtils.effacerMessage(view.messageErreur);;
        view.listePokemon.getSelectionModel().clearSelection();
        pokemonTrouverListe = null;
        view.bouttonCapturer.setDisable(true);

        if (recherche == null || recherche.trim().isEmpty()) {
            MessageUtils.afficherMessage(view.messageErreur, "Veuillez entrer un ID ou un nom.");
            return;
        }

        new Thread(() -> {
            try {
                Pokemon pokemon = service.recuperer(recherche.trim());
                pokemonTrouverListe = pokemon;

                Platform.runLater(() -> {
                    afficherPokemonDetails(pokemon);

                });

            } catch (ApiPokemonException e) {
                Platform.runLater(() -> {
                    MessageUtils.afficherMessage(view.messageErreur, "Erreur requête: " + e.getMessage());
                    clearPokemonDetails();
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    MessageUtils.afficherMessage(view.messageErreur, "Erreur inattendue : " + e.getMessage());
                    clearPokemonDetails();
                });
            }
        }).start();
    }

    public void capturerPokemon() {
        if (pokemonTrouverListe == null) {
            return;
        }
        if (!new ConfirmationBox().boite("Capturer ce Pokémon?", "Confirmation")) {
            return;
        }
        new Thread(() -> {
            try {
                dao.sauvegarder(pokemonTrouverListe);
                Platform.runLater(() -> {
                    raffraichirListe();
                    view.listePokemon.getSelectionModel().select(pokemonTrouverListe);
                    view.bouttonCapturer.setDisable(true);
                    view.champRecherche.clear();
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    MessageUtils.afficherMessage(view.messageErreur, "Pokémon introuvable ou erreur d'API : " + e.getMessage());
                });
            }
        }).start();
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
        if (!new ConfirmationBox().boite("Voulez vous vraiment relacher ce Pokémon ?", "Confirmation")) {
            return;
        }

        final Pokemon selectionFinal = selectionList;

        new Thread(() -> {
            try{
                dao.relacherPokemon(selectionFinal.id);
                Platform.runLater(() -> {
                    raffraichirListe();
                    view.bouttonRelacher.setDisable(true);
                    pokemonTrouverListe = null;
                    MessageUtils.effacerMessage(view.messageErreur);;
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    MessageUtils.afficherMessage(view.messageErreur, "Erreur lors du relachement : " + e.getMessage());
                });
            }
        }).start();
    }

    public void afficherPokemonDetails(Pokemon pokemon) {
        pokemonActuel = pokemon;
        if (pokemon == null) {
            clearPokemonDetails();
            return;
        }

        view.conteneurTypes.getChildren().clear();
        view.lblPokemonIdNom.setText(pokemon.toString());

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
        view.bouttonFavori.setDisable(false);
        majAffichageBouttonFavori(pokemon);

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
        view.bouttonFavori.setDisable(true);
        view.bouttonFavori.setText("☆ Favori");

    }

    public void raffraichirListe() {
        try {
            pokemonListe = dao.lister();
        } catch (Exception e) {
            MessageUtils.afficherMessage(view.messageErreur, "Erreur dans la base de données : " + e.getMessage());
        }
        view.listePokemon.getItems().setAll(pokemonListe);
        view.nombrePokemons.setText("Vous avez capturé "+ pokemonListe.size() + " Pokémons");
    }

    public void demarrer() {
        raffraichirListe();
        MessageUtils.effacerMessage(view.messageErreur);
        view.montrerChampTexte();
    }


    // Aide de copilot
    private void toggleFavori(){
        Pokemon cible = pokemonTrouverListe;
        if (cible == null) {
            cible =view.listePokemon.getSelectionModel().getSelectedItem();
        }

        cible.favori = !cible.favori;

        try{
            dao.sauvegarder(cible);
        } catch (Exception e){
            MessageUtils.afficherMessage(view.messageErreur, "Erreur lors de la mise a jour du favori: "+e.getMessage());
            return;
        }

        majAffichageBouttonFavori(cible);

        raffraichirListe();

        for (Pokemon pokemon : view.listePokemon.getItems()) {
            if (pokemon.id == cible.id) {
                view.listePokemon.getSelectionModel().select(pokemon);
                break;
            }
        }
    }

    // Aide de copilot
    private void majAffichageBouttonFavori(Pokemon pokemon) {
        if (pokemon != null && pokemon.favori) {
            view.bouttonFavori.setText("* Favori");
        } else {
            view.bouttonFavori.setText("- Favori");
        }
    }
}
