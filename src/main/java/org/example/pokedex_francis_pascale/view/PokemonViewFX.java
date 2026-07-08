package org.example.pokedex_francis_pascale.view;

import org.example.pokedex_francis_pascale.modele.Pokemon;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;



public class PokemonViewFX {


    public final ListView<Pokemon> listePokemon;
    public final TextArea detail;
    public final TextField champUuid;
    public final Button btnCharger;
    public final Label messageErreur;
    public final ImageView imagePokemon;
    private final BorderPane racine;


    public PokemonViewFX() throws Exception {
        imagePokemon = new ImageView();
        imagePokemon.setFitWidth(200);
        imagePokemon.setPreserveRatio(true);

        // Zone a gauche
        listePokemon = new ListView<>();
        listePokemon.setPrefWidth(300);

        // Zone centre
        detail = new TextArea();
        detail.setEditable(false);
        detail.setPromptText("Selection un Pokemon pour voir ses details");
        detail.setWrapText(true);

        BorderPane zoneDetails = new BorderPane();
        zoneDetails.setRight(imagePokemon);
        zoneDetails.setCenter(detail);



        // Zone du bas
        champUuid = new TextField();
        champUuid.setPromptText("Donnez le UUID d'un film a partir de l'API");
        HBox.setHgrow(champUuid, Priority.ALWAYS);

        btnCharger = new Button("Charger depuis API");

        messageErreur = new Label();
//        messageErreur.setStyle("-fx-text-fill:red");


        HBox formulaire = new HBox(10, champUuid, btnCharger);
        VBox bas = new VBox(6, formulaire, messageErreur);

        //Assemblage
        racine = new BorderPane();
        racine.setLeft(listePokemon);
        racine.setCenter(zoneDetails);
        racine.setBottom(bas);
        imagePokemon.fitWidthProperty().bind(racine.widthProperty().multiply(0.25));
        BorderPane.setMargin(bas, new Insets(15, 0,0,0));
        racine.setPadding(new Insets(5));
    }

    public Parent getRoot() {
        return racine;
    }
}
