package it.uninsubria.centrivaccinali.util;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.controller.Controller;
import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    private FadeTransition ft;

    public DialogHelper(String fxmlName, Controller parentController, Pane rootPane) {
        FXMLLoader fxmlLoader = new FXMLLoader(CentriVaccinali.class.getResource(fxmlName + ".fxml"));
        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(parent);
        stage = new Stage();
        Controller c = fxmlLoader.getController();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        if(fade) {
            ft = new FadeTransition(Duration.millis(2000), parentController.);
            ft.setFromValue(1.0);
            ft.setToValue(0.1);
            ft.play();
        }

        // Trascinamento finestra
        scene.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });
        scene.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - xOffset);
            stage.setY(mouseEvent.getScreenY() - yOffset);
        });

        stage.showAndWait();
    }

}
