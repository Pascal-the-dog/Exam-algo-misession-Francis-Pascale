package org.example.pokedex_francis_pascale.utils;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
public class ConfirmationBox {
    public boolean boite(String message, String titre) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setResizable(false);
        dialog.setTitle(titre);

        Label l = new Label(message);
        l.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");

        Button btnconfirmer = new Button("Confirmer");
        Button btnannuler = new Button("Annuler");

        final boolean[] result = {false};

        btnconfirmer.setOnAction(e -> {
            result[0] = true;
            dialog.close();
        });

        btnannuler.setOnAction(e -> {
            dialog.close();
        });

        HBox bouttons = new HBox(15, btnconfirmer, btnannuler);
        bouttons.setAlignment(Pos.CENTER);

        VBox layout = new VBox(20, l, bouttons);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle(
                "-fx-padding: 20;" +
                        "-fx-background-color: #3173d2;"
        );

        Scene scene = new Scene(layout);
        dialog.setScene(scene);

        try {
            String cssPath = getClass().getResource("/styles.css").toExternalForm();
            scene.getStylesheets().add(cssPath);
        } catch (NullPointerException e) {
            System.err.println("CSS expansion warning: Could not find /styles.css in the resources folder.");
        }

        dialog.showAndWait();
        return result[0];
    }
}