package it.uninsubria.centrivaccinali.client;

import it.uninsubria.centrivaccinali.controller.CVLoginController;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.server.ServerCVInterface;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ClientCV extends UnicastRemoteObject implements ClientCVInterface {

    private static final long serialVersionUID = 1L;
    private static Registry reg = null;
    private static ServerCVInterface server=null;
    private CVLoginController sourceCVlogin;

    public static void setRegistry(Registry reg) {
        ClientCV.reg = reg;
    }

    public static void setServer(ServerCVInterface server) {
        ClientCV.server = server;
    }

    public ClientCV() throws RemoteException {
        //si occupa il thread di ottenere la connessione
        ConnectionThread connChecker=new ConnectionThread();
    }

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
