package it.uninsubria.centrivaccinali.client;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.util.Objects;

public class Window extends Application {

    /** Valore larghezza stage della finestra di avvio. */
    private final static Double w_avvio = 450.0;

    /** Valore altezza stage della finestra di avvio. */
    private final static Double h_avvio = 280.0;

    /** Valore larghezza stage della finestra login operatore sanitario. */
    private final static Double w_cv_login = 390.0;

    /** Valore altezza stage della finestra login operatore sanitario. */
    private final static Double h_cv_login = 420.0;

    /** Valore altezza stage della finestra registra centro vaccinale. */
    private final static Double w_cv_regc = 800.0;

    /** Valore altezza stage della finestra registra centro vaccinale. */
    private final static Double h_cv_regc = 350.0;

    /** Valore altezza stage della finestra registra vaccinato. */
    private final static Double w_cv_recv = 500.0;

    /** Valore altezza stage della finestra registra vaccinato. */
    private final static Double h_cv_recv = 600.0;

    /** Valore larghezza stage della finestra registrazione cittadino. */
    private final static Double w_ci_reg = 400.0;

    /** Valore altezza stage della finestra registrazione cittadino. */
    private final static Double h_ci_reg = 520.0;

    /** Valore larghezza stage della finestra dashoboard cittadino. */
    private final static Double w_dashboard = 800.0;

    /** Valore altezza stage della finestra dashoboard cittadino. */
    private final static Double h_dashboard = 664.0;

    /** Valore larghezza stage standard. */
    private final static Double w_standard = 390.0;

    /** Valore altezza stage standard. */
    private final static Double h_standard = 565.0;

    /**
     * Scene dell'applicazione.
     * @see Scene
     */
    public static Scene scene;

    /**
     * Stage dell'applicazione.
     * @see Stage
     */
    public static Stage stage;

    /** Variabile larghezza per adattare la dimensione della finestra al contenuto. */
    private static Double width;

    /** Variabile ltezza per adattare la dimensione della finestra al contenuto. */
    private static Double height;

    /** Posizione x del mouse durante operazione di trascinamento della finestra. */
    private double xOffset;

    /** Posizione y del mouse durante operazione di trascinamento della finestra. */
    private double yOffset;

    public static void open() {
        launch( );
    }


    /**
     * Metodo di avvio dell'interfaccia grafica con prima schermata Avvio.fxml.
     * @param s stage principale.
     */
    @Override
    public void start(Stage s) {
        stage = s;
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
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Metodo per selezionare l'interfaccia desiderata
     * @param fxml oggetto che identifica il file FXML dell'interfaccia.
     */
    public static void setRoot(String fxml) {
        scene.setRoot(loadFXML(fxml));
        switch (fxml) {
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
                width = w_cv_regc;
                height = h_cv_regc;
                break;
            case "CV_registraVaccinato":
                stage.setTitle("Registra un nuovo vaccinato");
                width = w_cv_recv;
                height = h_cv_recv;
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
            default:
                System.err.println("[ATTENZIONE] NOME FXML ERRATO");
                break;
        }
        stage.setWidth(width);
        stage.setHeight(height);
        stage.centerOnScreen();
    }


    /**
     * Metodo per caricare il file fxml selezionato durante il metodo setRoot
     * @param fxml percorso al file fxml desiderato
     * @return ritorna fxmlLoader caricato
     */
    public static Parent loadFXML(String fxml) {
        FXMLLoader fxmlLoader = new FXMLLoader(CentriVaccinali.class.getResource("fxml/" + fxml + ".fxml"));
        try {
            return fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
