package it.uninsubria.centrivaccinali.client;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.server.ServerCVInterface;
import it.uninsubria.centrivaccinali.util.DialogHelper;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

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
                    ClientCV.setServer(server);
                    System.out.println("[ConnectionThread] Connessione al server eseguita");
                    break;
                }
                mySleep();
            }
            if (!status) {
                System.err.println("[ConnectionThread] Non e' stato possibile effettuare la connessione con il server");
                Platform.runLater(() -> {
                    DialogHelper dh = new DialogHelper("ERRORE DI CONNESSIONE", "L'applicazione non e' riuscita a connettersi al server \n Vuoi riprovare a connetterti?", DialogHelper.Type.ERROR);
                    Button b = new Button("SI");
                    b.setOnAction( eh -> {
                        this.start();
                    });
                    dh.addButton(b);
                    dh.display((Pane) CentriVaccinali.scene.getRoot());
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
