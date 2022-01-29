package it.uninsubria.centrivaccinali.client.controller.cittadini.dashboard;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.controller.Controller;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.Result;
import it.uninsubria.centrivaccinali.util.CssHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;

/**
 * Contoller dell'interfaccia che mostra le informazioni del centro vaccinale selezionato.
 * @author ...
 */
public class CIInfoCentroController extends Controller {


    /**
     * <code>AnchorPane</code> contenitore della seguente interfaccia.
     * @see AnchorPane
     */
    @FXML private AnchorPane ap_root;


    /**
     * <code>FontIcon</code> che mostra una icona per il centro selezionato.
     * @see FontIcon
     */
    @FXML private FontIcon fi_iconaCentro;


    /**
     * <code>Label</code> che mostra la tipologia del centro selezionato.
     * @see Label
     */
    @FXML private Label l_tipologia;


    /**
     * <code>Label</code> che mostra il nome del centro selezionato.
     * @see Label
     */
    @FXML private Label l_nome;


    /**
     * <code>Label</code> che mostra l'indirizzo del centro selezionato.
     * @see Label
     */
    @FXML private Label l_indirizzo;


    /**
     * <code>AnchorPane</code> che contiene le sotto-interfacce.
     * @see AnchorPane
     */
    @FXML private AnchorPane ap_container;


    /**
     * <code>Button</code> per mostrare l'interfaccia con gli eventi avversi registrati.
     * @see CISegnalazioniController
     */
    @FXML private Button b_segnalazioni;


    /**
     * <code>Button</code> per mostrare l'interfaccia con il grafico.
     * @see CIGraficiController
     */
    @FXML private Button b_grafico;


    /**
     * Rifermento al singleton <code>CssHelper</code> che permette la gestione degli stili per i vari componenti grafici.
     * @see CssHelper
     */
    private final CssHelper css = CssHelper.getInstance();


    /**
     * Contenitore dell'interfaccia con gli eventi registrati.
     * @see CISegnalazioniController
     */
    private AnchorPane ap_segnalazioni;


    /**
     * Contenitore dell'interfaccia con il grafico.
     * @see CIGraficiController
     */
    private AnchorPane ap_grafico;


    /**
     * Centro vaccinale selezionato.
     */
    private CentroVaccinale cv;


    /**
     Contenitore dell'interfaccia con gli eventi registrati.
     * @see CISegnalazioniController
     */
    private CIDashboardController parent;


    //Metodo ereditato dalla superclasse
    @Override
    public void notifyController(Result result) {  }

    /**
     * Setta i dati del centro selezionato.
     * @param cv il centro vaccinale selezionato.
     */
    public void setData(CentroVaccinale cv) {
        this.cv = cv;
        switch(cv.getTipologia()) {
            case OSPEDALIERO:
                fi_iconaCentro.setIconLiteral("mdi2h-hospital-building:128:#3456e3");
                break;
            case HUB:
                fi_iconaCentro.setIconLiteral("mdi2h-hospital-marker:128:#c148eb");
                break;
            case AZIENDALE:
                fi_iconaCentro.setIconLiteral("mdi2f-factory:128:#323232");
                break;
        }
        l_tipologia.setText(cv.getTipologia().toString());
        l_nome.setText(cv.getNome());
        l_indirizzo.setText(cv.getIndirizzo().toString());

        mostraGrafico();
    }


    /**
     * Mostra l'interfaccia delle segnalazioni.
     * @see CISegnalazioniController
     */
    @FXML
    private void mostraSegnalazioni() {
        css.toggle(b_segnalazioni, b_grafico);
        if (ap_segnalazioni != null) {
            Platform.runLater(() -> {
                if (ap_grafico != null)
                    ap_grafico.setVisible(false);
                ap_segnalazioni.setVisible(true);
            });
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(CentriVaccinali.class.getResource("fxml/fragments/prospetto/SegnalazioniEventiAvversi.fxml"));
            try {
                AnchorPane ap = fxmlLoader.load();
                ap_segnalazioni = ap;
                Platform.runLater(() -> {
                    if (ap_grafico != null)
                        ap_grafico.setVisible(false);
                    ap_container.getChildren().add(ap);
                });
                CISegnalazioniController c = fxmlLoader.getController();
                c.setData(cv);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Metodo per mostrarare l'interfaccia con il grafico.
     * @see CIGraficiController
     */
    @FXML
    private void mostraGrafico() {
        css.toggle(b_grafico, b_segnalazioni);
        if (ap_grafico != null) {
            Platform.runLater(() -> {
                if (ap_segnalazioni != null)
                    ap_segnalazioni.setVisible(false);
                ap_grafico.setVisible(true);
            });
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(CentriVaccinali.class.getResource("fxml/fragments/prospetto/GraficoEventiAvversi.fxml"));
            try {
                AnchorPane ap = fxmlLoader.load();
                ap_grafico = ap;
                Platform.runLater(() -> {
                    if (ap_segnalazioni != null)
                        ap_segnalazioni.setVisible(false);
                    ap_container.getChildren().add(ap);
                });
                ((CIGraficiController) fxmlLoader.getController()).setData(cv);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Permette di rimuovere quest'interfaccia dalla dashboard.
     * @see CIDashboardController
     */
    @FXML
    private void chiudiFragment() {
        parent.rimuoviFragment(ap_root);
    }


    /**
     * Setta il riferimento alla dashboard che contiene la seguente interfaccia.
     * @see CIDashboardController
     * @param c controller dell'interfaccia contenitore.
     */
    public void setParent(Controller c) {
        parent = (CIDashboardController) c;
    }

}