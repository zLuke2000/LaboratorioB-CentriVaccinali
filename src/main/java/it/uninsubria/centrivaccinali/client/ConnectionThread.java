package it.uninsubria.centrivaccinali.client;

import it.uninsubria.centrivaccinali.util.DialogHelper;
import it.uninsubria.server.ServerCVInterface;
import javafx.application.Platform;
import javafx.scene.control.Button;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Classe per la gestione della connessione con il server.
 * @author ...
 */
public class ConnectionThread extends Thread{


    /**
     * Riferimento al registry.
     */
    private Registry reg;


    /**
     * Riferimento all'oggetto remoto del server.
     */
    private ServerCVInterface server;


    /**
     * Costruttore primario per il thread.
     */
    public ConnectionThread(){
        start();
    }


    /**
     * Metodo <code>run</code> del thread.
     * Provo ad ottenere la connessione al server recuperando il riferimento allo stub.
     * Se non e' possibile ottenere la connessione al server viene mostrato un pop-up d'errore.
     */
    public void run(){
        boolean status = false;
        try {
            for (int i = 0; i < 12; i++) {
                status = getRegistry() && getServerStub();
                if (status) {
                    //setta nel client il registry e il server
                    ClientCV.setServer(server);
                    System.out.println("[ConnectionThread] Connessione al server eseguita");
                    break;
                } else {
                    System.err.println("[ConnectionThread] Tentativo di connessione n." + (i + 1));
                }
                Thread.sleep(5000);
            }
            if (!status) {
                System.err.println("[ConnectionThread] Non e' stato possibile effettuare la connessione con il server");
                Platform.runLater(() -> {
                    DialogHelper dh = new DialogHelper("ERRORE DI CONNESSIONE dentro al thread", "L'applicazione non e' riuscita a connettersi al server \n Vuoi riprovare a connetterti?", DialogHelper.Type.ERROR);
                    Button b = new Button("SI");
                    b.setOnAction( eh -> this.start());
                    dh.addButton(b);
                    dh.display();
                });
            }
        } catch (InterruptedException e) {
            System.err.println("Il thread e' stato interrotto");
        }
    }


    /**
     * Metodo per ottenere riferimento al registry.
     * @return booleano che rappresenta l'esito dell'operazione.
     */
    private boolean getRegistry() {
        try {
            reg = LocateRegistry.getRegistry(Registry.REGISTRY_PORT);
            return true;
        } catch (RemoteException e) {
            System.err.println("[ConnectionThread] non e' stato possibile trovare il registro RMI");
            return false;
        }
    }


    /**
     * Metodo per ottenere il riferimento allo stub del server.
     * @return booleano che rappresenta l'esito dell'operazione.
     */
    private boolean getServerStub(){
        try {
            server = (ServerCVInterface) reg.lookup("server");
            return true;
        } catch (RemoteException | NotBoundException e) {
            System.err.println("[ConnectionThread] non e' stato possibile trovare la chiave nel registro RMI");
            return false;
        }
    }
}
