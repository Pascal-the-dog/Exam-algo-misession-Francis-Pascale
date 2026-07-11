package org.example.pokedex_francis_pascale.view;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import org.example.pokedex_francis_pascale.modele.Pokemon;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;



public class PokemonViewFX {
    public final ComboBox<String> selecteurOption;
    public ComboBox<String> comboRecherche;
    public TextField champRecherche;
    public final Button bouttonRecherche;
    public final Button bouttonCapturer;
    public final Button bouttonRelacher;
    public final Button bouttonFavori;
    public final Label lblPokemonIdNom;
    public final Label valleurHp, valleurAttack, valleurAttackSp, valleurDefense, valleurDefenseSp, valleurVitesse;
    public final ProgressBar barHp, barAttack, barAttackSp, barDefense, barDefenseSp, barVitesse;
    public final ImageView imagePokemon;
    public final ListView<Pokemon> listePokemon;
    public final Label messageErreur;
    public final Label nombrePokemons;
    public final HBox conteneurTypes;
    private final BorderPane racine;

    // Bar de stats
    ProgressBar StatBar() {
        ProgressBar bar = new ProgressBar(0.0);
        bar.setPrefWidth(140);
        bar.setMaxWidth(Double.MAX_VALUE);
        return bar;
    }

    public void montrerChampTexte() {
        champRecherche.setVisible(true);
        champRecherche.setManaged(true);
        comboRecherche.setVisible(false);
        comboRecherche.setManaged(false);
    }

    public void montrerCombo() {
        champRecherche.setVisible(false);
        champRecherche.setManaged(false);
        comboRecherche.setVisible(true);
        comboRecherche.setManaged(true);
    }


    void LigneStats(GridPane grid, String nomStat, Label labelValeur, ProgressBar bar, int ligne) {
        Label nameLabel = new Label(nomStat);
        nameLabel.setPrefWidth(110);
        labelValeur.setPrefWidth(35);
        labelValeur.setAlignment(Pos.CENTER_RIGHT);

        grid.add(nameLabel, 0, ligne);
        grid.add(labelValeur, 1, ligne);
        grid.add(bar, 2, ligne);
        GridPane.setHgrow(bar, Priority.ALWAYS);


        GridPane.setHgrow(bar, Priority.ALWAYS);
    }

    public PokemonViewFX() {

        // Bar et bouton de recherche
        Label lblRecherchePar = new Label("Recherche Par : ");
        selecteurOption = new ComboBox<>();
        selecteurOption.getItems().addAll("ID / Nom", "Favoris", "Type", "Génération");
        selecteurOption.setValue("ID / Nom");
        selecteurOption.setPrefWidth(120);

        champRecherche = new TextField();

        champRecherche.setPromptText("Trouver un pokemon");
        HBox.setHgrow(champRecherche, Priority.ALWAYS);

        comboRecherche = new ComboBox<>();
        comboRecherche.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(comboRecherche, Priority.ALWAYS);

        bouttonRecherche = new Button("Trouver");
        bouttonRecherche.setStyle("-fx-cursor: hand;");
        champRecherche.setOnAction(e -> bouttonRecherche.fire());
        bouttonCapturer = new Button("Capturer");
        bouttonCapturer.setStyle("-fx-cursor: hand;");
        bouttonCapturer.setDisable(true);

        bouttonRelacher = new Button("Relâcher");
        bouttonRelacher.setStyle("-fx-cursor: hand;");
        bouttonRelacher.setDisable(true);
        bouttonRelacher.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(bouttonRelacher, new Insets(5, 0, 0,0));

        bouttonFavori = new Button("☆ Favori");
        bouttonFavori.setStyle("-fx-cursor: hand;");
        bouttonFavori.setDisable(true);
        bouttonFavori.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(bouttonFavori, new Insets(5, 0, 0,0));


        HBox sectionRecherche = new HBox(10, lblRecherchePar, selecteurOption, champRecherche, comboRecherche, bouttonRecherche, bouttonCapturer);
        sectionRecherche.setAlignment(Pos.TOP_LEFT);
        sectionRecherche.setPadding(new Insets(5, 5, 20, 5));

        // image de pokemon, elle devrait etre au centre a gauche
        imagePokemon = new ImageView();
        imagePokemon.setPreserveRatio(true);
        imagePokemon.setSmooth(true);

        // Barre de chargement
        ProgressBar barChargementImage = new ProgressBar();
        barChargementImage.setMaxWidth(Double.MAX_VALUE);
        barChargementImage.setVisible(false);

        StackPane ImagePokemonPlaceholder = new StackPane();
        ImagePokemonPlaceholder.getChildren().addAll(imagePokemon, barChargementImage);
        StackPane.setAlignment(barChargementImage, Pos.CENTER);
        ImagePokemonPlaceholder.setStyle(
                "-fx-border-color: #facc15;" +
                "-fx-border-width: 3.5;" +
                "-fx-border-radius: 14;" +
                "-fx-background-radius: 14;" +
                "-fx-background-color: linear-gradient(to bottom, #ffffff, #f5f5f5);" +
                "-fx-background-insets: 0, 2;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.35), 14, 0.3, 0, 4);"
        );
        imagePokemon.fitWidthProperty().bind(ImagePokemonPlaceholder.widthProperty());
        imagePokemon.fitHeightProperty().bind(ImagePokemonPlaceholder.heightProperty());

        ImagePokemonPlaceholder.setMinSize(0, 0);
        ImagePokemonPlaceholder.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        HBox.setHgrow(ImagePokemonPlaceholder, Priority.ALWAYS);

        // Section de statistiques, elle devrait se trouver au centre
        lblPokemonIdNom = new Label("Aucun Pokemon Selectioner");
        lblPokemonIdNom.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        conteneurTypes = new HBox(6);
        conteneurTypes.setAlignment(Pos.CENTER_LEFT);
        conteneurTypes.setMaxWidth(Region.USE_PREF_SIZE);
        conteneurTypes.getStyleClass().add("types-container-box");

        Label lblStatsTitre = new Label("Statistiques");
        lblStatsTitre.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        GridPane PanneauStats = new GridPane();

        valleurHp = new Label("-");
        barHp = StatBar();
        valleurAttack = new Label("-");
        barAttack = StatBar();
        valleurAttackSp = new Label("-");
        barAttackSp = StatBar();
        valleurDefense = new Label("-");
        barDefense = StatBar();
        valleurDefenseSp = new Label("-");
        barDefenseSp = StatBar();
        valleurVitesse = new Label("-");
        barVitesse = StatBar();

        LigneStats(PanneauStats, "Point de vie: ", valleurHp, barHp, 0);
        LigneStats(PanneauStats, "Attaque: ", valleurAttack, barAttack, 1);
        LigneStats(PanneauStats, "Attaque Special: ", valleurAttackSp, barAttackSp, 2);
        LigneStats(PanneauStats, "Defense: ", valleurDefense, barDefense, 3);
        LigneStats(PanneauStats, "Defense Special: ", valleurDefenseSp, barDefenseSp, 4);
        LigneStats(PanneauStats, "Vitesse: ", valleurVitesse, barVitesse, 5);


        PanneauStats.setHgap(12);
        PanneauStats.setVgap(10);


        HBox ligneIdentite = new HBox(10);
        ligneIdentite.setAlignment(Pos.CENTER_LEFT);

        Region spacerIdentite = new Region();
        HBox.setHgrow(spacerIdentite, Priority.ALWAYS);

        ligneIdentite.getChildren().addAll(lblPokemonIdNom, spacerIdentite, conteneurTypes);

        VBox PanneauInfoCentral = new VBox(12, ligneIdentite, lblStatsTitre, PanneauStats);
        PanneauInfoCentral.setPadding(new Insets(0, 15, 0, 15));

        VBox.setVgrow(PanneauInfoCentral, Priority.ALWAYS);

        // Section de pokemon sauvegarder dans la base de donnee
        VBox PanneauListe = new VBox(5);
        Label lblListeTitre = new Label("Liste de vos Pokemons");
        lblListeTitre.setStyle("-fx-font-weight: bold;");
        listePokemon = new ListView<>();
        listePokemon.setCellFactory(lv -> new ListCell<Pokemon>() {
            @Override
            protected void updateItem(Pokemon item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                }
            }
        });

        nombrePokemons = new Label();

        VBox.setVgrow(listePokemon, Priority.ALWAYS);
        PanneauListe.getChildren().addAll(lblListeTitre, listePokemon, bouttonRelacher, bouttonFavori, nombrePokemons);
        PanneauListe.setPrefWidth(250);
        PanneauListe.setMinWidth(220);

        //Assemblage principal
        HBox LePokedex = new HBox(10, ImagePokemonPlaceholder, PanneauInfoCentral, PanneauListe);
        HBox.setHgrow(PanneauInfoCentral, Priority.ALWAYS);

        HBox.setHgrow(LePokedex, Priority.ALWAYS);
        VBox.setVgrow(LePokedex, Priority.ALWAYS);

        messageErreur = new Label();
        messageErreur.setStyle("-fx-text-fill: red;");

        VBox conteneurPrincipal = new VBox(5, LePokedex, messageErreur);
        VBox.setVgrow(conteneurPrincipal, Priority.ALWAYS);


        racine = new BorderPane();
        racine.setPadding(new Insets(15));
        racine.setTop(sectionRecherche);
        racine.setCenter(conteneurPrincipal);

        lblRecherchePar.getStyleClass().add("texte-interface-blanc");
        lblListeTitre.getStyleClass().add("texte-interface-blanc");
        lblPokemonIdNom.getStyleClass().add("texte-titre-pokemon");
        lblStatsTitre.getStyleClass().add("texte-titre-pokemon");
        messageErreur.setId("messageErreur");

        try {
            String cssPath = getClass().getResource("/styles.css").toExternalForm();
            racine.getStylesheets().add(cssPath);
        } catch (NullPointerException e) {
            System.err.println("CSS expansion warning: Could not find /styles.css in the resources folder.");
        }
        Platform.runLater(() -> champRecherche.requestFocus());

    }
    public Parent getRoot(){
        return racine;
    }


}
