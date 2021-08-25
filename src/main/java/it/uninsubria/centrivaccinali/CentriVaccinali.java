package it.uninsubria.centrivaccinali;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import java.io.IOException;

public class CentriVaccinali extends Application {

    private final static Double h_cvlogin = 565.0;
    private final static Double w_cvlogin = 390.0;

    private static Scene scene;
    private static Stage stage;
    private static Double width;
    private static Double height;

    @Override
    public void start(Stage s) {
        CentriVaccinali.stage = s;
        scene = new Scene(loadFXML("Avvio"));
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setScene(scene);
        stage.setTitle("Progetto LaboratorioB");
        stage.show();
    }

    public static void setRoot(String fxml) {
        scene.setRoot(loadFXML(fxml));
        switch(fxml) {
            case "CV_login":
                stage.setTitle("Login operatore");
                width = w_cvlogin;
                height = h_cvlogin;
                break;
            default:
                System.err.println("[ATTENZIONE] NOME FXML ERRATO");
                break;
        }
        stage.setWidth(width);
        stage.setHeight(height);
    }

    public static Parent loadFXML(String fxml) {
        FXMLLoader fxmlLoader = new FXMLLoader(CentriVaccinali.class.getResource("fxml/" + fxml + ".fxml"));
        try {
            return fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        launch();
    }
}