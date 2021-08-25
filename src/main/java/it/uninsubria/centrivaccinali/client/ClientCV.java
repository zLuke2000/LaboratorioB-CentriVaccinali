package it.uninsubria.centrivaccinali.client;

import it.uninsubria.centrivaccinali.server.ServerCV;
import it.uninsubria.centrivaccinali.server.ServerCVInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ClientCV extends UnicastRemoteObject implements ClientCVInterface {

    private static final long serialVersionUID = 1L;
    private static Registry reg = null;
    private static ServerCVInterface server;

    public ClientCV() throws RemoteException {
        // connessione a RMI
        try {
            reg = LocateRegistry.getRegistry(1099);
        } catch (RemoteException e) {
            System.err.println("Non e' stato possibile trovare il registro RMI");
            e.printStackTrace();
            System.exit(0);
        }
        // download riferimento server
        try {
            server = (ServerCVInterface) reg.lookup("server");
            System.out.println("Connessione al server eseguita con successo");
        } catch (RemoteException | NotBoundException e) {
            System.err.println("Non e' stato possibile trovare la chiave nel registro RMI");
            e.printStackTrace();
            System.exit(0);
        }
    }

    public boolean autenticaOperatore(String username, String password) {
        System.out.println("[CLIENT] richiesta di autenticazione da parte di: " + username);
        try {
            if(server.autenticaOperatore(username, password)) {
                return true;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }
}
