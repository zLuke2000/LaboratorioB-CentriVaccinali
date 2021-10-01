package it.uninsubria.centrivaccinali.client;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.controller.CVLoginController;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.Cittadino;
import it.uninsubria.centrivaccinali.server.ServerCVInterface;
import it.uninsubria.centrivaccinali.util.AlertConnection;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Optional;

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

    private ConnectionThread connThread;

    public static void setRegistry(Registry reg) {
        ClientCV.reg = reg;
    }

    public static void setServer(ServerCVInterface server) {
        ClientCV.server = server;
    }

    public ClientCV() throws RemoteException {
        //si occupa il thread di ottenere la connessione
        connThread = new ConnectionThread();
    }


    public void autenticaOperatore(CVLoginController source, String username, String password) {
        this.sourceCVlogin = source;
        if(server != null) {
            try {
                server.authOperatore(this, username, password);
            } catch (RemoteException e) {
                System.err.println("[ClientCV] non e' stato possibile autenticare l'opertatore");
                lanciaPopup();
            }
        } else {
            System.err.println("[ClientCV] connessione al server assente");
            lanciaPopup();
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
            //TODO popup connessione server
            e.printStackTrace();
            return -2;
        }
    }

    private void printout(String s) {
        System.out.println("[CLIENT_CV] " + s);
    }

    private void lanciaPopup(){
//        Platform.runLater(() -> {
//            AlertConnection alert=new AlertConnection("Non connesso al server,"+"\nvuoi provare a connetterti?");
//            Optional<ButtonType> result = alert.showAndWait();
//            if (result.get()==ButtonType.YES){
//                //interrompi il thread e fallo ripartire
//                //per provare ad ottenere la connessione
//                if (connThread.isAlive()) {
//                    connThread.interrupt();
//                }
//                connThread=new ConnectionThread();
//            }
//            else {
//                alert.close();
//            }
//        });
        try {
            FXMLLoader fxmlLoader=new FXMLLoader(CentriVaccinali.class.getResource("fxml/dialogs/D_connectionError.fxml"));
            DialogPane connectionDialog=fxmlLoader.load();
            Dialog<ButtonType> dialog=new Dialog<>();
            dialog.setDialogPane(connectionDialog);
            dialog.setTitle("ERRORE");
            Optional<ButtonType> clickedButton=dialog.showAndWait();
            if (clickedButton.get()==ButtonType.YES){
                if (connThread.isAlive()) connThread.interrupt();
                connThread=new ConnectionThread();
            } else {
                dialog.close();
            }
        } catch (IOException e) {
            System.err.println("[ClientCV] errore durante la creazione del dialog");
            e.printStackTrace();
        }
    }

    public void registraCittadino(Cittadino cittadino) throws RemoteException {
        server.registraCittadino(cittadino);
    }
}
