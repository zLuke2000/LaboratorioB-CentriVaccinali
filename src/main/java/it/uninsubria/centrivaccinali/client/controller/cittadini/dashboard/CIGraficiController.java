//Centore Luca 740951 VA
//Lattarulo Luca 742597 VA
//Marelli Samuele 742495 VA
//Pintonello Christian 741112 VA
package it.uninsubria.centrivaccinali.client.controller.cittadini.dashboard;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.client.controller.Controller;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.Result;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import java.util.Map;

/**
 * Controller del grafico che mostra un prospetto riassuntivo degli eventi registrati.
 * @author Centore Luca 740951
 * @author Lattarulo Luca 742597
 * @author Marelli Samuele 742495
 * @author Pintonello Christian 741112
 */
public class CIGraficiController extends Controller {


    /**
     * Grafico a barre che mostra un riassunto degli eventi avversi registrati.
     */
    @FXML private BarChart<String, Double> barChart;


    /**
     * La serie di dati per il vaccino "pfizer".
     * @see javafx.scene.chart.XYChart.Series
     */
    private final XYChart.Series<String, Double> pfizer = new XYChart.Series<>();


    /**
     * La serie di dati per il vaccino "j&amp;j".
     * @see javafx.scene.chart.XYChart.Series
     */
    private final XYChart.Series<String, Double> jnj = new XYChart.Series<>();


    /**
     * La serie di dati per il vaccino "astrazeneca".
     * @see javafx.scene.chart.XYChart.Series
     */
    private final XYChart.Series<String, Double> astrazeneca = new XYChart.Series<>();


    /**
     * La serie di dati per il vaccino "moderna".
     * @see javafx.scene.chart.XYChart.Series
     */
    private final XYChart.Series<String, Double> moderna = new XYChart.Series<>();


    /**
     * Riferimento al client su cui si sta eseguendo l'applicazione.
     * @see ClientCV
     */
    private final ClientCV client = CentriVaccinali.client;


    /**
     * Metodo per inizializzare l'interfaccia.
     */
    @FXML
    private void initialize() {
        pfizer.setName("pfizer");
        jnj.setName("j&j");
        astrazeneca.setName("astrazeneca");
        moderna.setName("moderna");
    }


    /**
     * Notifica l'interfaccia dopo aver ottenuto dal server i dati riguardanti gli eventi avversi.
     * @param result l'operazione appena completata.
     */
    @Override
    public void notifyController(Result result) {
        if (result != null && result.getResult() && result.getOpType() == Result.Operation.LEGGI_EVENTI_AVVERSI) {
            for (Map.Entry<String, Double> entry : result.getMap().entrySet()) {
                String[] parts = entry.getKey().split("/");
                String vaccino = parts[0];
                String evento = parts[1];
                Double value = entry.getValue();
                switch (vaccino) {
                    case "pfizer" -> aggiungiEvento(pfizer, evento.replace(" ", "\n"), value);
                    case "j&j" -> aggiungiEvento(jnj, evento.replace(" ", "\n"), value);
                    case "moderna" -> aggiungiEvento(moderna, evento.replace(" ", "\n"), value);
                    case "astrazeneca" -> aggiungiEvento(astrazeneca, evento.replace(" ", "\n"), value);
                }
            }
            Platform.runLater(() -> {
                barChart.getData().add(pfizer);
                barChart.getData().add(jnj);
                barChart.getData().add(moderna);
                barChart.getData().add(astrazeneca);
            });
        }
    }


    /**
     * Aggiunge uno specifico evento al grafico.
     * @param series serie di dati in cui si vuole inserire l'evento.
     * @param evento la tipologia di evento.
     * @param media il valore associato al dato da inserire.
     */
    private void aggiungiEvento(XYChart.Series<String, Double> series, String evento, Double media) {
        Platform.runLater(() -> series.getData().add(new XYChart.Data<>(evento, media)));
    }


    /**
     * Effettua una chiamata al server per leggere gli eventi avversi registrati per il dato centro vaccinale.
     * @param cv centro vaccinale di cui si vogliono leggere gli eventi avversi.
     */
    public void setData(CentroVaccinale cv) {
        client.leggiEA(this, cv.getNome());
    }
}
