package it.uninsubria.centrivaccinali.client;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.controller.CVLoginController;
import it.uninsubria.centrivaccinali.controller.CVRegistraCittadinoController;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.Cittadino;
import it.uninsubria.centrivaccinali.models.Vaccinato;
import it.uninsubria.centrivaccinali.server.ServerCVInterface;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Optional;

public class ClientCV extends UnicastRemoteObject implements ClientCVInterface {

    private static final long serialVersionUID = 1L;
    private static Registry reg = null;
    private static ServerCVInterface server=null;
    private CVLoginController sourceCVlogin;
    private CVRegistraCittadinoController sourceCVRegCittadino;

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
        //sourceCVlogin.authStatus(ritorno);
    }

    @Override
    public void risultato(List<String> resultComuni, List<CentroVaccinale> resultCentri, int resultRegistrazione) throws RemoteException {
        if(resultComuni != null) {
            sourceCVRegCittadino.risultatoComuni(resultComuni);
        } else if(resultCentri != null) {
            sourceCVRegCittadino.risultatoCentri(resultCentri);
        } else if(resultRegistrazione != -1) {
            sourceCVRegCittadino.risultatoRegistrazione(resultRegistrazione);
        }
    }

    public int registraCentroVaccinale(CentroVaccinale cv) {
        try {
            return server.registraCentro(cv);
        } catch (RemoteException e) {
            System.err.println("[ClientCV] non e' stato possibile registrare il centro vaccinale");
            lanciaPopup();
            return -2;
        }
    }

    public void registraCittadino(Cittadino cittadino) {
        try {
            server.registraCittadino(cittadino);
        } catch (RemoteException e) {
            System.err.println("[ClientCV] registrazione cittadino fallita");
            lanciaPopup();
        }
    }

    public void loginUtente(String username, String password){
        try {
            server.loginUtente(this, username, password);
        } catch (RemoteException e) {
            System.err.println("[ClientCV] Login Utente fallito");
            lanciaPopup();
        }
    }

    public void registraVaccinato(Vaccinato vaccinato) {
        try {
            server.registraVaccinato(vaccinato);
        } catch (RemoteException e) {
            System.err.println("[ClientCV] registrazione vaccinato fallita");
            lanciaPopup();
        }
    }

    public void getComuni(CVRegistraCittadinoController cvrcc, String provincia) {
        sourceCVRegCittadino = cvrcc;
        try {
            server.getComuni(this, provincia);
        } catch (RemoteException e) {
            System.err.println("[ClientCV] impossibile effettuare la ricerca dei comuni");
            lanciaPopup();
        }
    }

    public void getCentri(CVRegistraCittadinoController cvrcc, String comune) {
        sourceCVRegCittadino = cvrcc;
        try {
            server.getCentri(this, comune);
        } catch (RemoteException e) {
            System.err.println("[ClientCV] Impossibile effettuare la ricerca dei centri");
            lanciaPopup();
        }
    }

    private void printout(String s) {
        System.out.println("[CLIENT_CV] " + s);
    }

    private void lanciaPopup(){
        try {
            FXMLLoader fxmlLoader=new FXMLLoader(CentriVaccinali.class.getResource("fxml/dialogs/D_connectionError.fxml"));
            DialogPane connectionDialog=fxmlLoader.load();
            Dialog<ButtonType> dialog=new Dialog<>();
            dialog.setDialogPane(connectionDialog);
            dialog.setTitle("ERRORE");
            Optional<ButtonType> clickedButton=dialog.showAndWait();
            if (clickedButton.get()==ButtonType.YES){
                //faccio ripartire il thread che si occupa di ottenere
                //la connessione (lo interrompo se e' ancora attivo)
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
}
