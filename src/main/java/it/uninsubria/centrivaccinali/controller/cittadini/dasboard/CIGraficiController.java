package it.uninsubria.centrivaccinali.controller.cittadini.dasboard;

import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.controller.Controller;
import it.uninsubria.centrivaccinali.models.Result;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class CIGraficiController extends Controller {

    private ClientCV client;


    @FXML
    private BarChart<String, Integer> ci_bc_prospetto;

    @FXML
    private CategoryAxis tipoEventoAxis;

    @FXML
    private NumberAxis gravitaAxis;

    @FXML
    void initialize() {
        tipoEventoAxis.setLabel("Tipo evento avverso");
        gravitaAxis.setLabel("Gravità");

        XYChart.Series pfizer = new XYChart.Series();
        pfizer.setName("pfizer");
        pfizer.getData().add(new XYChart.Data("Mal di testa", 1));
        pfizer.getData().add(new XYChart.Data("Febbre", 3));

        XYChart.Series jnj = new XYChart.Series();
        jnj.setName("j&j");
        jnj.getData().add(new XYChart.Data("Mal di testa", 4));
        jnj.getData().add(new XYChart.Data("Febbre", 2));

        ci_bc_prospetto.getData().addAll(pfizer, jnj);
    }

    @Override
    public void initParameter(ClientCV client) {
        this.client = client;
    }

    @Override
    public void notifyController(Result result) {

    }
}
