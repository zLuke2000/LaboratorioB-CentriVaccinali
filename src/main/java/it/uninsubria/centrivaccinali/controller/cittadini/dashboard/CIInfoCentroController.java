package it.uninsubria.centrivaccinali.controller.cittadini.dashboard;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.controller.Controller;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.Result;
import it.uninsubria.centrivaccinali.util.CssHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;

/**
 *
 */
public class CIInfoCentroController extends Controller {
    /**
     *
     */
    @FXML private AnchorPane ap_root;
    /**
     *
     */
    @FXML private FontIcon fi_iconaCentro;
    /**
     *
     */
    @FXML private Label l_tipologia;
    /**
     *
     */
    @FXML private Label l_nome;
    /**
     *
     */
    @FXML private Label l_indirizzo;
    /**
     *
     */
    @FXML private AnchorPane ap_container;
    /**
     *
     */
    @FXML private Button b_segnalazioni;
    /**
     *
     */
    @FXML private Button b_grafico;
    /**
     *
      */
    private final CssHelper css = CssHelper.getInstance();
    /**
     *
     */
    private AnchorPane ap_segnalazioni;
    /**
     *
     */
    private AnchorPane ap_grafico;
    /**
     *
     */
    private CentroVaccinale cv;
    /**
     *
     */
    private CIDashboardController parent;

    /**
     *
     * @param result
     */
    @Override
    public void notifyController(Result result) {  }

    /**
     *
     * @param cv
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
     *
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
     *
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
     *
     */
    @FXML
    private void chiudiFragment() {
        parent.rimuoviFragment(ap_root);
    }

    /**
     *
     * @param c
     */
    public void setParent(Controller c) {
        parent = (CIDashboardController) c;
    }

}