package it.uninsubria.centrivaccinali.client;

import it.uninsubria.centrivaccinali.server.ServerCVInterface;

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
        boolean result=false;
            for (int i = 0; i < 2; i++) {
                result = getRegistry() && getServerStub();
                if (result) {
                    //setta nel client il registry e il server
                    ClientCV.setRegistry(reg);
                    ClientCV.setServer(server);
                    System.out.println("[ConnectionThread] Connessione al server eseguita");
                    break;
                }
                mySleep();
            }
            if (!result)
                System.err.println("[ConnectionThread] Non e' stato possibile effettuare la connessione con il server");
    }

    private boolean getRegistry(){
        try {
            reg = LocateRegistry.getRegistry(1099);
            return true;
        } catch (RemoteException e) {
            System.err.println("[ConnectionThread] non e' stato possibile trovare il registro RMI");
            e.printStackTrace();
            return false;
        }
    }

    private boolean getServerStub(){
        try {
            server = (ServerCVInterface) reg.lookup("server");
            return true;
        } catch (RemoteException | NotBoundException e) {
            System.err.println("[ConnectionThread] non e' stato possibile trovare la chiave nel registro RMI");
            e.printStackTrace();
            return false;
        }
    }

    private void mySleep(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {  }
    }
}
