package it.uninsubria.centrivaccinali.client;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.controller.*;
import it.uninsubria.centrivaccinali.enumerator.TipologiaCentro;
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
    private static ServerCVInterface server = null;
    private Cittadino utenteLoggato = null;
    private ConnectionThread connThread;

    private Controller controller;

    public ClientCV() throws RemoteException {
        //si occupa il thread di ottenere la connessione
        connThread = new ConnectionThread();
    }

    public Cittadino getUtenteLoggato() {
        return utenteLoggato;
    }

    public static void setRegistry(Registry reg) {
        ClientCV.reg = reg;
    }

    public static void setServer(ServerCVInterface server) {
        ClientCV.server = server;
    }

    private boolean connectionStatus() {
        if (server == null) {
            printerr("connessione al server assente");
            lanciaPopup();
            return false;
        }
        return true;
    }

    public void autenticaOperatore(CVLoginController cvLoginController, String username, String password) {
        controller=cvLoginController;
        if(connectionStatus()) {
            try {
                server.authOperatore(this, username, password);
            } catch (RemoteException e) {
                printerr("non e' stato possibile autenticare l'opertatore");
                lanciaPopup();
            }
        }
    }

    @Override
    public void notifyStatus(Result ritorno) throws RemoteException  {
        switch(ritorno.getOpType()) {
            case Result.LOGIN_UTENTE:
                utenteLoggato= ritorno.getCittadino();
                controller.notifyController(ritorno);
                break;
            case Result.LOGIN_OPERATORE:
            case Result.REGISTRAZIONE_VACCINATO:
            case Result.REGISTRAZIONE_CENTRO:
            case Result.REGISTRAZIONE_CITTADINO:
            case Result.RISULTATO_COMUNI:
            case Result.RISULTATO_CENTRI:
            case Result.RICERCA_CENTRO:
                controller.notifyController(ritorno);
                break;
            default:
                printerr("errore opType");
        }
    }

    public void registraCentroVaccinale(CVRegistraCentroVaccinale cvRegistraCentroVaccinale, CentroVaccinale cv) {
        controller=cvRegistraCentroVaccinale;
        try {
            server.registraCentro(this, cv);
        } catch (RemoteException e) {
            printerr("non e' stato possibile registrare il centro vaccinale");
            lanciaPopup();
        }
    }

    public void registraCittadino(CIRegistrazioneController ciRegistrazioneController, Cittadino cittadino) {
        controller=ciRegistrazioneController;
        if (connectionStatus()) {
            try {
                server.registraCittadino(this, cittadino);
            } catch (RemoteException e) {
                printerr("registrazione cittadino fallita");
                lanciaPopup();
            }
        }
    }

    public void loginUtente(Controller controller,String username, String password){
        this.controller = controller;
        if (connectionStatus()) {
            try {
                server.loginUtente(this, username, password);
            } catch (RemoteException e) {
                printerr("Login Utente fallito");
                lanciaPopup();
            }
        }
    }

    public void ricercaPerNome(CIDashboardController ciDashboardController, String nome) {
        controller=ciDashboardController;
        if (connectionStatus()) {
            try {
                server.ricercaCentroPerNome(this, nome);
            } catch (RemoteException e) {
                printerr("ricerca fallita");
                lanciaPopup();
            }
        }
    }

    public void ricercaPerComuneTipologia(CIDashboardController ciDashboardController, String comune, TipologiaCentro tipologia) {
        controller=ciDashboardController;
        if (connectionStatus()) {
            try {
                server.ricercaCentroPerComuneTipologia(this, comune, tipologia);
            } catch (RemoteException e) {
                printerr("ricerca fallita");
                lanciaPopup();
            }
        }
    }

    public void registraVaccinato(CVRegistraCittadinoController cvRegistraCittadinoController, Vaccinato vaccinato) {
        controller=cvRegistraCittadinoController;
        try {
            server.registraVaccinato(this, vaccinato);
        } catch (RemoteException e) {
            printerr("registrazione vaccinato fallita");
            lanciaPopup();
        }
    }

    public void getComuni(CVRegistraCittadinoController cvRegistraCittadinoController, String provincia) {
        controller = cvRegistraCittadinoController;
        try {
            server.getComuni(this, provincia);
        } catch (RemoteException e) {
            printerr("impossibile effettuare la ricerca dei comuni");
            lanciaPopup();
        }
    }

    public void getCentri(CVRegistraCittadinoController cvRegistraCittadinoController, String comune) {
        controller = cvRegistraCittadinoController;
        try {
            server.getCentri(this, comune);
        } catch (RemoteException e) {
            printerr("Impossibile effettuare la ricerca dei centri");
            lanciaPopup();
        }
    }

    public void stopOperation() {
        if (server != null) {
            try {
                server.stopThread();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
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
