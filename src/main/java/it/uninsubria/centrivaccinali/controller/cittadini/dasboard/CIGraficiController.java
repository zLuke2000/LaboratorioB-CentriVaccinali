package it.uninsubria.centrivaccinali.controller.cittadini.dasboard;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.controller.Controller;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.Result;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.Map;

public class CIGraficiController extends Controller {

    private final ClientCV client = CentriVaccinali.client;
    private CentroVaccinale cv;
    private CIInfoCentroController parent;

    private final XYChart.Series<String, Double> pfizer = new XYChart.Series<>();
    private final XYChart.Series<String, Double> jnj = new XYChart.Series<>();
    private final XYChart.Series<String, Double> astrazeneca = new XYChart.Series<>();
    private final XYChart.Series<String, Double> moderna = new XYChart.Series<>();

    @FXML
    private BarChart<String, Double> barChart;
    @FXML
    private CategoryAxis tipoEventoAxis;
    @FXML
    private NumberAxis gravitaAxis;

    @FXML
    void initialize() {
//        ci_bc_prospetto.setAnimated(false);
//
//        tipoEventoAxis.setLabel("Tipo evento avverso");
//        gravitaAxis.setLabel("Gravità");

        pfizer.setName("pfizer");

        jnj.setName("j&j");

        astrazeneca.setName("astrazeneca");

        moderna.setName("moderna");
    }

    @Override
    public void notifyController(Result result) {
        if (result != null && result.getResult() && result.getOpType() == Result.Operation.LEGGI_EVENTI_AVVERSI) {
            for (Map.Entry<String, Double> entry : result.getMap().entrySet()) {
                String[] parts = entry.getKey().split("/");
                String vaccino = parts[0];
                String evento = parts[1];
                Double value = entry.getValue();
                switch (vaccino) {
                    case "pfizer":
                        aggiungiEvento(pfizer, evento.replace(" ", "\n"), value);
                        break;
                    case "j&j":
                        aggiungiEvento(jnj, evento.replace(" ", "\n"), value);
                        break;
                    case "moderna":
                        aggiungiEvento(moderna, evento.replace(" ", "\n"), value);
                        break;
                    case "astrazeneca":
                        aggiungiEvento(astrazeneca, evento.replace(" ", "\n"), value);
                        break;
                }
            }
            Platform.runLater(() -> barChart.getData().addAll(pfizer, jnj, moderna, astrazeneca));
        }
    }

    private void aggiungiEvento(XYChart.Series<String, Double> series, String evento, Double x) {
        Platform.runLater(() -> series.getData().add(new XYChart.Data<>(evento, x)));
    }
    
    public void setData(CentroVaccinale cv) {
        client.leggiEA(this, cv.getNome());
    }

    public void setParent(Controller c) {
        parent = (CIInfoCentroController) c;
    }
}
