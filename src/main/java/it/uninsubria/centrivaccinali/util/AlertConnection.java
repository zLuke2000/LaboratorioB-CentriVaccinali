package it.uninsubria.centrivaccinali.util;

import javafx.scene.control.*;

public class AlertConnection extends Alert {

    public AlertConnection() {
        super(AlertType.ERROR, "Connessione con il server persa." + "\n" + "Riprova?", ButtonType.CLOSE, ButtonType.YES);
    }

    public AlertConnection(AlertType alertType) {
        super(alertType);
    }

    public AlertConnection(AlertType alertType, String contentText, ButtonType... buttons) {
        super(alertType, contentText, buttons);
    }
}
