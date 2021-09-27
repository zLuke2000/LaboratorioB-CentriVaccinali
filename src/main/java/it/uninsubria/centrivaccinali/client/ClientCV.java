package it.uninsubria.centrivaccinali.client;

import it.uninsubria.centrivaccinali.controller.CVLoginController;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.Cittadino;
import it.uninsubria.centrivaccinali.server.ServerCVInterface;
import javafx.application.Platform;

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
        ConnectionThread connChecker = new ConnectionThread();
    }


    public void autenticaOperatore(CVLoginController source, String username, String password) {
        this.sourceCVlogin = source;
        if(server != null) {
            try {
                server.authOperatore(this, username, password);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            // TODO ALERT
            //Platform.runLater();
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

    public int registraCentroVaccinale(CentroVaccinale cv) {
        try {
            return server.registraCentro(cv);
        } catch (RemoteException e) {
            e.printStackTrace();
            return -2;
        }
    }

    private void printout(String s) {
        System.out.println("[CLIENT_CV] " + s);
    }

    public void registraCittadino(Cittadino cittadino) {
        server.registraCittadino(cittadino);
    }
}
