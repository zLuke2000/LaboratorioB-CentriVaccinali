package it.uninsubria.centrivaccinali;

import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.controller.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Objects;

public class CentriVaccinali extends Application {

    private final static Double h_avvio = 300.0;
    private final static Double w_avvio = 580.0;
    private final static Double h_cvlogin = 565.0;
    private final static Double w_cvlogin = 390.0;
    private final static Double h_cvchange = 565.0;
    private final static Double w_cvchange = 390.0;

    private static Scene scene;
    private static Stage stage;
    private static Double width;
    private static Double height;
    private static ClientCV client;
    private static FXMLLoader fxmlLoader;

    @Override
    public void start(Stage s) {
        CentriVaccinali.stage = s;
        scene = new Scene(Objects.requireNonNull(loadFXML("Avvio")));
        stage.setScene(scene);
        stage.setTitle("Progetto LaboratorioB");
        stage.setResizable(false);
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
                CIHomeController cihc = fxmlLoader.getController();
                cihc.initParameter(client);
                stage.setTitle("Progetto LaboratorioB");
                break;
            case "CV_login":
                System.out.println("[CV_MAIN] selezionato: CV_login");
                CVLoginController cvlc = fxmlLoader.getController();
                cvlc.initParameter(client);
                stage.setTitle("Login operatore");
                width = w_cvlogin;
                height = h_cvlogin;
                break;
            case "CV_change":
                System.out.println("[CV_MAIN] selezionato: CV_change");
                stage.setTitle("Seleziona azione");
                width = w_cvchange;
                height = h_cvchange;
                break;
            case "CV_registraCentroVaccinale":
                System.out.println("[CV_MAIN] selezionato: CV_registraCentroVaccinale");
                CVRegistraCentroVaccinale cvrcv = fxmlLoader.getController();
                cvrcv.initParameter(client);
                stage.setTitle("Registra nuovo centro vaccinale");
                width = 800.0;
                height = 350.0;
                break;
            case "CV_registraVaccinato":
                stage.setTitle("Registra un nuovo vaccinato");
                width=600.0;
                height=560.0;
                break;
            case "CI_registrazione":
                CIRegistrazioneController circ = fxmlLoader.getController();
                circ.initParameter(client);
                stage.setTitle("Registrazione cittadino");
                width = 390.0;
                height = 640.0;
            default:
                System.err.println("[ATTENZIONE] NOME FXML ERRATO");
                break;
        }
        stage.setWidth(width);
        stage.setHeight(height);
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