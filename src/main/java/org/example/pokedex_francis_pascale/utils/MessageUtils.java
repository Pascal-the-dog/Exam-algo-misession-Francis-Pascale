package org.example.pokedex_francis_pascale.utils;

import javafx.scene.control.Label;

public class MessageUtils {

    public static void afficherMessage(Label label, String message) {
        label.setText(message);
        label.setVisible(true);
    }

    public static void effacerMessage(Label label) {
        label.setText("");
        label.setVisible(false);
    }
}
