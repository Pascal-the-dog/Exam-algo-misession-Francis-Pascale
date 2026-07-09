package org.example.pokedex_francis_pascale.view;

import org.example.pokedex_francis_pascale.modele.Pokemon;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;



public class PokemonViewFX {
    public final ComboBox<String> SelecteurOption;
    public final TextField champRecherche;
    public final Button bouttonRecherche;
    public final Label lblPokemonIdNom;
    public final Label valleurHp, valleurAttack, valleurAttackSp, valleurDefense, valleurDefenseSp, valleurVitesse;
    public final ProgressBar barHp, barAttack, barAttackSp, barDefense, barDefenseSp, barVitesse;
    public final ImageView imagePokemon;
    public final ListView<Pokemon> listePokemon;
    public final Label messageErreur;

    private final BorderPane racine;

    // Bar de stats
    ProgressBar StatBar() {
        ProgressBar bar = new ProgressBar(0.0);
        bar.setPrefWidth(140);
        bar.setMaxWidth(Double.MAX_VALUE);
        return bar;
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
    }

    public PokemonViewFX() {

        // Bar et bouton de recherche
        Label lblRecherchePar = new Label("Recherche Par : ");
        SelecteurOption = new ComboBox<>();
        SelecteurOption.getItems().addAll("ID / Nom", "Type");
        SelecteurOption.setValue("ID / Nom");
        SelecteurOption.setPrefWidth(120);

        champRecherche = new TextField();
        champRecherche.setPromptText("Trouver un pokemon");
        HBox.setHgrow(champRecherche, Priority.ALWAYS);

        bouttonRecherche = new Button("Trouver");
        bouttonRecherche.setStyle("-fx-cursor: hand;");

        HBox sectionRecherche = new HBox(10, lblRecherchePar, SelecteurOption, champRecherche, bouttonRecherche);
        sectionRecherche.setAlignment(Pos.TOP_LEFT);
        sectionRecherche.setPadding(new Insets(5,5,20,5));

        // image de pokemon, elle devrait etre au centre a gauche
        imagePokemon = new ImageView();
        imagePokemon.setFitWidth(200);
        imagePokemon.setFitHeight(200);
        imagePokemon.setPreserveRatio(true);

        StackPane ImagePokemonPlaceholder = new StackPane(imagePokemon);
        ImagePokemonPlaceholder.setStyle("-fx-border-color: #dddddd; -fx-border-width: 1.5; -fx-border-radius: 8; " +
                "-fx-background-radius: 8; -fx-background-color: #ffffff; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        ImagePokemonPlaceholder.setPrefSize(200, 200);
        ImagePokemonPlaceholder.setMinSize(200, 200);
        ImagePokemonPlaceholder.setAlignment(Pos.CENTER);

        // Section de statistiques, elle devrait se trouver au centre
        lblPokemonIdNom = new Label("Aucun Pokemon Selectioner");
        lblPokemonIdNom.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Label lblStatsTitre = new Label("Statistiques");
        lblStatsTitre.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        GridPane PanneauStats = new GridPane();

        valleurHp = new Label("-"); barHp = StatBar();
        valleurAttack = new Label("-"); barAttack = StatBar();
        valleurAttackSp = new Label("-"); barAttackSp = StatBar();
        valleurDefense = new Label("-"); barDefense = StatBar();
        valleurDefenseSp = new Label("-"); barDefenseSp = StatBar();
        valleurVitesse = new Label("-"); barVitesse = StatBar();

        LigneStats(PanneauStats, "Point de vie: ", valleurHp, barHp, 0);
        LigneStats(PanneauStats, "Attaque: ", valleurAttack, barAttack, 1);
        LigneStats(PanneauStats, "Attaque Special: ", valleurAttackSp, barAttackSp, 2);
        LigneStats(PanneauStats, "Defense: ", valleurDefense, barDefense, 3);
        LigneStats(PanneauStats, "Defense Special: ", valleurDefenseSp, barDefenseSp, 4);
        LigneStats(PanneauStats, "Vitesse: ", valleurVitesse, barVitesse, 5);


        PanneauStats.setHgap(12);
        PanneauStats.setVgap(10);
        PanneauStats.setAlignment(Pos.CENTER_LEFT);

        VBox PanneauInfoCentral = new VBox(12, lblPokemonIdNom, lblStatsTitre, PanneauStats);
        PanneauInfoCentral.setPadding(new Insets(0, 15, 0, 15));

        // Section de pokemon sauvegarder dans la base de donnee
        VBox PanneauListe = new VBox(5);
        Label lblListeTitre = new Label("List de vos pokemon");
        lblListeTitre.setStyle("-fx-font-weight: bold;");
        listePokemon = new ListView<>();
        VBox.setVgrow(listePokemon, Priority.ALWAYS);
        PanneauListe.getChildren().addAll(lblListeTitre, listePokemon);
        PanneauListe.setPrefWidth(250);
        PanneauListe.setMinWidth(220);

        //Assemblage principal
        HBox LePokedex = new HBox(10, ImagePokemonPlaceholder, PanneauInfoCentral, PanneauListe);
        HBox.setHgrow(PanneauInfoCentral, Priority.ALWAYS);

        messageErreur = new Label();
        messageErreur.setStyle("-fx-text-fill: red;");

        VBox conteneurPrincipal = new VBox(5, LePokedex, messageErreur);
        VBox.setVgrow(conteneurPrincipal, Priority.ALWAYS);


        racine = new BorderPane();
        racine.setPadding(new Insets(15));
        racine.setTop(sectionRecherche);
        racine.setCenter(conteneurPrincipal);
    }
    public Parent getRoot(){
        return racine;
    }
}
