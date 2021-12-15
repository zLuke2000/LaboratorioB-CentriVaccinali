package it.uninsubria.centrivaccinali.controller.cittadini.dasboard;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.controller.Controller;
import it.uninsubria.centrivaccinali.enumerator.Vaccino;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.EventoAvverso;
import it.uninsubria.centrivaccinali.models.Result;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CIGraficiController extends Controller {

    private ClientCV client = CentriVaccinali.client;
    private CentroVaccinale cv;
    private CIInfoCentroController parent;
    private List<String> listaEA = new ArrayList<>();

    private XYChart.Series pfizer;
    private XYChart.Series jnj;
    private XYChart.Series astrazeneca;
    private XYChart.Series moderna;

    @FXML
    private BarChart<String, Integer> ci_bc_prospetto;
    @FXML
    private CategoryAxis tipoEventoAxis;
    @FXML
    private NumberAxis gravitaAxis;

    @FXML
    void initialize() {
        ci_bc_prospetto.setAnimated(false);

        tipoEventoAxis.setLabel("Tipo evento avverso");
        gravitaAxis.setLabel("Gravità");

        pfizer = new XYChart.Series();
        pfizer.setName("pfizer");

        jnj = new XYChart.Series();
        jnj.setName("j&j");
        System.out.println(jnj);

        astrazeneca = new XYChart.Series();
        astrazeneca.setName("astrazeneca");
        System.out.println(astrazeneca);

        moderna = new XYChart.Series();
        moderna.setName("moderna");

        listaEA.add("mal di testa");
        listaEA.add("febbre");
        listaEA.add("dolori muscolari e articolari");
        listaEA.add("linfoadenopatia");
        listaEA.add("tachicardia");
        listaEA.add("crisi chipertensiva");
    }

    @Override
    public void initParameter(ClientCV client) {
        this.client = client;
    }

    @Override
    public void notifyController(Result result) {
        if (result != null & result.getResult() & result.getOpType() == Result.Operation.LEGGI_EVENTI_AVVERSI) {
            for (Map.Entry<String, Double> entry : result.getMap().entrySet()) {
                String[] parts = entry.getKey().split("/");
                String vaccino = parts[0];
                String evento = parts[1];
                Double value = entry.getValue();
                if (vaccino.equals("pfizer")) {
                     aggiungiEvento(pfizer, evento, value);
                } else if (vaccino.equals("j&j")) {
                    aggiungiEvento(jnj, evento, value);
                } else if (vaccino.equals("moderna")) {
                    aggiungiEvento(moderna, evento, value);
                } else if (vaccino.equals("astrazeneca")) {
                    aggiungiEvento(astrazeneca, evento, value);
                }
            }
            Platform.runLater(() -> {
                ci_bc_prospetto.getData().addAll(pfizer, jnj, astrazeneca, moderna);
            });
        }
    }

    private void aggiungiEvento(XYChart.Series series, String evento, Double x) {
        System.out.println("Entra");
        Platform.runLater(() -> {
            System.out.println("aggiunto: " + series + " " + evento + " = " + x);
            series.getData().add(new XYChart.Data(evento, x));
//            if (listaEA.contains(evento)) {
//                System.out.println("aggiunto: " + series + " " + evento + " = " + x);
//                series.getData().add(new XYChart.Data(evento, x));
//            } else {
//                //series.getData().add(new XYChart.Data("altro", x));
//            }
        });
    }
    
    public void setData(CentroVaccinale cv) {
        client.leggiEA(this, cv.getNome());
    }

    public void setParent(Controller c) {
        parent = (CIInfoCentroController) c;
    }
}
