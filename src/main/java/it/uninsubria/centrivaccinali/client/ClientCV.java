package it.uninsubria.centrivaccinali.client;

import it.uninsubria.centrivaccinali.controller.CVLoginController;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.server.ServerCVInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 */
public class ClientCV extends UnicastRemoteObject implements ClientCVInterface {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private static Registry reg = null;

    /**
     *
     */
    private static ServerCVInterface server;

    /**
     *
     */
    private CVLoginController sourceCVlogin;

    /**
     *
     * @throws RemoteException
     */
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
            printout("Connessione al server eseguita con successo");
        } catch (RemoteException | NotBoundException e) {
            printout("Non e' stato possibile trovare la chiave nel registro RMI");
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     *
     * @param source
     * @param username
     * @param password
     */
    public void autenticaOperatore(CVLoginController source, String username, String password) {
        this.sourceCVlogin = source;
        try {
            server.authOperatore(this, username, password);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyStatus(boolean ritorno) throws RuntimeException {
        if (ritorno) {
            printout("AUTH OK");
        }
        else {
            printout("AUTH KO");
        }
        sourceCVlogin.authStatus(ritorno);
    }

    public boolean registraCentroVaccinale(CentroVaccinale cv) {
        try {
            server.registraCentro(cv);
            return true;
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }


    private void printout(String s) {
        System.out.println("[CLIENT_CV] " + s);
    }
}
