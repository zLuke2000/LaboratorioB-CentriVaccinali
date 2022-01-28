package it.uninsubria.centrivaccinali.util;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.Window;
import it.uninsubria.centrivaccinali.controller.dialog.GenericDialogController;
import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

/**
 * Classe per la gestione e creazione di dialog.
 * @author Luca Centore
 */
public class DialogHelper {


    /**
     * Riferimento allo stage dell'applicazione
     */
    private final Stage stage;


    /**
     * Transizione per rendere trasparente l'interfaccia sottostante.
     */
    private final FadeTransition ft = new FadeTransition(Duration.millis(500));


    /**
     * Root al momento attiva nell'applicazione
     */
    private final Pane rootPane = (Pane) Window.scene.getRoot();


    /**
     * Riferimento al controller del Dialog
     */
    private final GenericDialogController gdc;


    /**
     * Riferimento al parent dell'applicazione
     */
    private Parent parent;


    /**
     *  Riferimento sull'asse delle ascisse della finestra
     */
    private Double xOffset;


    /**
     * Riferimento sull'asse delle ordinate della finestra
     */
    private Double yOffset;


    /**
     * Costruttore di <code>DialogHelper</code>, crea un dialog specificando il titolo, la descrizione ed il tipo.
     * @param titolo titolo del dialog.
     * @param descrizione descrizione da mostrare nel dialog.
     * @param tipo tipologia del dialog.
     */
    public DialogHelper(String titolo, String descrizione, Type tipo) {
        FXMLLoader fxmlLoader = new FXMLLoader(CentriVaccinali.class.getResource("fxml/dialogs/D_generic.fxml"));
        System.out.println();
        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(parent);
        stage = new Stage();
        gdc = fxmlLoader.getController();

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);

        // Imposto lo stile della finestra
        switch (tipo) {
            case INFO:
                gdc.ap_root.getStylesheets().add(String.valueOf(CentriVaccinali.class.getResource("style/dialog/InfoStyle.css")));
                break;
            case WARNING:
                gdc.ap_root.getStylesheets().add(String.valueOf(CentriVaccinali.class.getResource("style/dialog/WarningStyle.css")));
                break;
            case ERROR:
                gdc.ap_root.getStylesheets().add(String.valueOf(CentriVaccinali.class.getResource("style/dialog/ErrorStyle.css")));
                break;
        }
        // Imposto titolo della finestra
        gdc.l_d_title.setText(titolo);
        // Imposto contenuto della finestra
        gdc.l_d_description.setText(descrizione);
        // Imposto riferimento a questo oggetto
        gdc.setDH(this);

        // Trascinamento finestra
        scene.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });
        scene.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - xOffset);
            stage.setY(mouseEvent.getScreenY() - yOffset);
        });
    }


    /**
     * Metodo per mostrare il dialog.
     */
    public void display() {
        ft.setNode(rootPane);
        ft.setFromValue(1.0);
        ft.setToValue(0.1);
        ft.play();
        stage.showAndWait();
    }


    /**
     * Permette l'aggiunta di un nuovo bottone.
     * @param b bottone da aggiungere.
     */
    public void addButton(Button b) {
        gdc.addButton(b);
    }


    /**
     * Permette di chiudere il dialog.
     */
    public void close() {
        stage.close();
        ft.stop();
        ft.setNode(rootPane);
        ft.setFromValue(0.1);
        ft.setToValue(1.0);
        ft.play();
    }


    /**
     * Classe enumerativa per i vari tipi di dialog.
     */
    public enum Type {
        /**
         * Dialog di tipo informazione
         */
        INFO,
        /**
         * Dialog di tipo attenzione
         */
        WARNING,
        /**
         * Dialog di tipo errore
         */
        ERROR
    }
}
