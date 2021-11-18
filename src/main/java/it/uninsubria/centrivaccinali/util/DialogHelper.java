package it.uninsubria.centrivaccinali.util;

import it.uninsubria.centrivaccinali.CentriVaccinali;
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

public class DialogHelper {

    private Parent parent;
    private Double xOffset;
    private Double yOffset;
    private Stage stage;
    private FadeTransition ft = new FadeTransition(Duration.millis(2000));
    private Pane rootPane;
    private GenericDialogController gdc;

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
     * @param rootPane can be null
     */
    public void display(Pane rootPane) {
        this.rootPane = rootPane;
        if(rootPane != null) {
            ft.setNode(rootPane);
            ft.setFromValue(1.0);
            ft.setToValue(0.1);
            ft.play();
        }
        stage.showAndWait();
    }

    public void addButton(Button b) {
        gdc.addButton(b);
    }

    public void close() {
        stage.close();
        if(rootPane != null) {
            ft.setNode(rootPane);
            ft.setFromValue(0.1);
            ft.setToValue(1.0);
            ft.play();
        }
    }

    public enum Type {
        INFO, WARNING, ERROR
    }
}
