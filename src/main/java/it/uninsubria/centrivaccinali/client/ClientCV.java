package it.uninsubria.centrivaccinali.client;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.controller.CIHomeController;
import it.uninsubria.centrivaccinali.controller.CIRegistrazioneController;
import it.uninsubria.centrivaccinali.controller.CVLoginController;
import it.uninsubria.centrivaccinali.controller.CVRegistraCittadinoController;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.Cittadino;
import it.uninsubria.centrivaccinali.models.Result;
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
import java.util.Optional;

public class ClientCV extends UnicastRemoteObject implements ClientCVInterface {

    private static final long serialVersionUID = 1L;
    private static Registry reg = null;
    private static ServerCVInterface server=null;
    private CVLoginController sourceCVlogin;
    private CVRegistraCittadinoController sourceCVRegCittadino;
    private CIHomeController sourceCIhome;
    private CIRegistrazioneController sourceCIregistrazione;
    private Cittadino utenteLoggato = null;
    private ConnectionThread connThread;

    public Cittadino getUtenteLoggato() {
        return utenteLoggato;
    }

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
                printerr("non e' stato possibile autenticare l'opertatore");
                lanciaPopup();
            }
        } else {
            printerr("connessione al server assente");
            lanciaPopup();
        }
    }

    @Override
    public void notifyStatus(Result ritorno) throws RemoteException  {
        switch(ritorno.getOpType()) {
            case Result.LOGIN_OPERATORE:
                sourceCVlogin.authStatus(ritorno.getResult());
                break;
            case Result.REGISTRAZIONE_VACCINATO:
                //TODO
                break;
            case Result.REGISTRAZIONE_CENTRO:
                break;
            case Result.LOGIN_UTENTE:
                utenteLoggato= ritorno.getCittadino();
                sourceCIhome.loginStatus(ritorno.getResult());
                break;
            case Result.REGISTRAZIONE_CITTADINO:
                sourceCVRegCittadino.risultatoRegistrazione(ritorno.getExtendedResult());
                break;
            case Result.RISULTATO_COMUNI:
                if(ritorno.getResultComuni() != null) {
                    sourceCVRegCittadino.risultatoComuni(ritorno.getResultComuni());
                }
                break;
            case Result.RISULTATO_CENTRI:
                if(ritorno.getResultCentri() != null) {
                    sourceCVRegCittadino.risultatoCentri(ritorno.getResultCentri());
                }
                break;
            default:
                printerr("errore opType");
        }
    }

    public void registraCentroVaccinale(CentroVaccinale cv) {
        try {
            server.registraCentro(this, cv);
        } catch (RemoteException e) {
            printerr("non e' stato possibile registrare il centro vaccinale");
            lanciaPopup();
        }
    }

    public void registraCittadino(CIRegistrazioneController ciRegistrazioneController, Cittadino cittadino) {
        sourceCIregistrazione=ciRegistrazioneController;
        try {
            server.registraCittadino(this,cittadino);
        } catch (RemoteException e) {
            printerr("registrazione cittadino fallita");
            lanciaPopup();
        }
    }

    public void loginUtente(CIHomeController ciHomeController,String username, String password){
        sourceCIhome=ciHomeController;
        try {
            server.loginUtente(this, username, password);
        } catch (RemoteException e) {
            printerr("Login Utente fallito");
            lanciaPopup();
        }
    }

    public void registraVaccinato(Vaccinato vaccinato) {
        try {
            server.registraVaccinato(this, vaccinato);
        } catch (RemoteException e) {
            printerr("registrazione vaccinato fallita");
            lanciaPopup();
        }
    }

    public void getComuni(CVRegistraCittadinoController cvrcc, String provincia) {
        sourceCVRegCittadino = cvrcc;
        try {
            server.getComuni(this, provincia);
        } catch (RemoteException e) {
            printerr("impossibile effettuare la ricerca dei comuni");
            lanciaPopup();
        }
    }

    public void getCentri(CVRegistraCittadinoController cvrcc, String comune) {
        sourceCVRegCittadino = cvrcc;
        try {
            server.getCentri(this, comune);
        } catch (RemoteException e) {
            printerr("Impossibile effettuare la ricerca dei centri");
            lanciaPopup();
        }
    }

    public void stopOperation() {
        try {
            server.stopThread();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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
            printerr("errore durante la creazione del dialog");
            e.printStackTrace();
        }
    }

    private void printout(String s) {
        System.out.println("[CLIENT_CV] " + s);
    }

    private void printerr(String s) {
        System.err.println("[CLIENT_CV] " + s);
    }

}
