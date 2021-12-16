package it.uninsubria.centrivaccinali;

import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.controller.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Objects;

public class CentriVaccinali extends Application {

    private final static Double w_avvio = 450.0;
    private final static Double h_avvio = 280.0;

    private final static Double w_cv_login = 390.0;
    private final static Double h_cv_login = 420.0;

    private final static Double w_ci_reg = 400.0;
    private final static Double h_ci_reg = 520.0;

    private final static Double w_dashboard = 800.0;
    private final static Double h_dashboard = 664.0;

    private final static Double w_standard = 390.0;
    private final static Double h_standard = 565.0;

    public static Scene scene;
    private static Stage stage;
    private static Double width;
    private static Double height;

    //TODO rimuovere initParameter
    public static ClientCV client;
    private static FXMLLoader fxmlLoader;
    private static Controller controller;

    private double xOffset;
    private double yOffset;

    @Override
    public void start(Stage s) {
        CentriVaccinali.stage = s;
        scene = new Scene(Objects.requireNonNull(loadFXML("Avvio")));
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });
        scene.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - xOffset);
            stage.setY(mouseEvent.getScreenY() - yOffset);
        });
        scene.setFill(Color.TRANSPARENT);
        stage.setTitle("Progetto LaboratorioB");
        stage.setResizable(false);
        //stage.centerOnScreen();
        stage.setScene(scene);
        stage.show();
}

    public static void setRoot(String fxml) {
        scene.setRoot(loadFXML(fxml));
        switch(fxml) {
            case "Avvio":
                stage.setTitle("Progetto LaboratorioB");
                height = h_avvio;
                width = w_avvio;
                break;
            case "CI_home":
                stage.setTitle("Progetto LaboratorioB");
                width = w_standard;
                height = h_standard;
                break;
            case "CV_login":
                System.out.println("[CV_MAIN] selezionato: CV_login");
                stage.setTitle("Login operatore");
                height = h_cv_login;
                width = w_cv_login;
                break;
            case "CV_home":
                System.out.println("[CV_MAIN] selezionato: CV_home");
                stage.setTitle("Seleziona azione");
                width = w_avvio;
                height = h_avvio;
                break;
            case "CV_registraCentroVaccinale":
                System.out.println("[CV_MAIN] selezionato: CV_registraCentroVaccinale");
                stage.setTitle("Registra nuovo centro vaccinale");
                width = 800.0;
                height = 350.0;
                break;
            case "CV_registraVaccinato":
                stage.setTitle("Registra un nuovo vaccinato");
                width = 500.0;
                height = 600.0;
                break;
            case "CI_registrazione":
                stage.setTitle("Registrazione cittadino");
                width = w_ci_reg;
                height = h_ci_reg;
                break;
            case "CI_dashboard":
                stage.setTitle("Area Cittadino");
                height = h_dashboard;
                width = w_dashboard;
                break;
            case "fragments/CI_F_showGeneralCV":
                stage.setTitle("Info CV");
                height = 800.0;
                width = 1200.0;
            default:
                System.err.println("[ATTENZIONE] NOME FXML ERRATO");
                break;
        }
        controller = fxmlLoader.getController();
        System.out.println("PROVA STAMPA CLIENTE " + client);
        stage.setWidth(width);
        stage.setHeight(height);
        stage.centerOnScreen();
    }

    /**
     * @param fxml
     * @return
     */
    public static Parent loadFXML(String fxml) {
        fxmlLoader = new FXMLLoader(CentriVaccinali.class.getResource("fxml/" + fxml + ".fxml"));
        try {
            return fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // Avvio Thread separato per ClientCV
        try {
            client = new ClientCV();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        // Avvio interfaccia grafica
        launch();
    }
}