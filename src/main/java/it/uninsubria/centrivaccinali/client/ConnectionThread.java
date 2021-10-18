package it.uninsubria.centrivaccinali.client;

import it.uninsubria.centrivaccinali.server.ServerCVInterface;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Optional;

/**
 * Classe per il controllo della connessione con il server
 */
public class ConnectionThread extends Thread{

    private Registry reg;
    private ServerCVInterface server;

    public ConnectionThread(){
        start();
    }

    public void run(){
        boolean status=false;
            for (int i = 0; i < 12; i++) {
                status = getRegistry() && getServerStub();
                if (status) {
                    //setta nel client il registry e il server
                    ClientCV.setRegistry(reg);
                    ClientCV.setServer(server);
                    System.out.println("[ConnectionThread] Connessione al server eseguita");
                    break;
                }
                mySleep();
            }
            if (!status) {
                System.err.println("[ConnectionThread] Non e' stato possibile effettuare la connessione con il server");
                Platform.runLater(() -> {
                    //TODO usare D_connectionError.fxml
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERRORE DI CONNESSIONE");
                    alert.setHeaderText("Errore");
                    alert.setContentText("Non è stato possibile connettersi al server, riconnettere?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get()==ButtonType.YES){
                        //fai ripartire il thread per provare ad
                        // ottenere la connessione
                        this.run();
                    }
                    else {
                        alert.close();
                    }
                });
            }
    }

    private boolean getRegistry(){
        try {
            reg = LocateRegistry.getRegistry(Registry.REGISTRY_PORT);
            return true;
        } catch (RemoteException e) {
            System.err.println("[ConnectionThread] non e' stato possibile trovare il registro RMI");
            return false;
        }
    }

    private boolean getServerStub(){
        try {
            server = (ServerCVInterface) reg.lookup("server");
            return true;
        } catch (RemoteException | NotBoundException e) {
            System.err.println("[ConnectionThread] non e' stato possibile trovare la chiave nel registro RMI");
            return false;
        }
    }

    private void mySleep(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ignored) {  }
    }
}
