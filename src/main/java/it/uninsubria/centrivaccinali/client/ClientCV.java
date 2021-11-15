package it.uninsubria.centrivaccinali.client;

import it.uninsubria.centrivaccinali.controller.*;
import it.uninsubria.centrivaccinali.enumerator.TipologiaCentro;
import it.uninsubria.centrivaccinali.models.*;
import it.uninsubria.centrivaccinali.server.ServerCVInterface;
import it.uninsubria.centrivaccinali.util.DialogHelper;
import javafx.scene.control.Button;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientCV extends UnicastRemoteObject implements ClientCVInterface {

    private static final long serialVersionUID = 1L;
    private static ServerCVInterface server = null;
    private Cittadino cittadinoConnesso = null;
    private ConnectionThread connThread;

    private Controller controller;

    public ClientCV() throws RemoteException {
        //si occupa il thread di ottenere la connessione
        connThread = new ConnectionThread();
    }

    public Cittadino getUtenteLoggato() {
        return cittadinoConnesso;
    }

    public void LogoutUtente() {
        this.cittadinoConnesso = null;
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
                controller.notifyController(new Result(false, Result.Operation.LOGIN_OPERATORE));
                lanciaPopup();
            }
        }
    }

    @Override
    public void notifyStatus(Result ritorno) throws RemoteException  {
        if (ritorno.getOpType() == Result.Operation.LOGIN_CITTADINO || ritorno.getOpType() == Result.Operation.REGISTRAZIONE_CITTADINO) {
            if (ritorno.getResult())
                cittadinoConnesso = ritorno.getCittadino();
        }
        controller.notifyController(ritorno);
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

    public void registraEventoAvverso(EAController eaController , EventoAvverso ea) {
        controller = eaController;
        try {
            server.registraEventoAvverso(this, ea, cittadinoConnesso.getId_vaccino());
        } catch (RemoteException e) {
            printerr("Impossibile registrare l'evento");
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

    private void lanciaPopup() {
        DialogHelper dh = new DialogHelper("ERRORE DI CONNESSIONE", "L'applicazione non e' attualmente connessa al server \n Vuoi provare a connetterti?", DialogHelper.Type.ERROR);
        Button b = new Button("SI");
        b.setOnAction( eh -> {
            if (connThread.isAlive()){
                connThread.interrupt();
            }
            connThread = new ConnectionThread();
        });
        dh.addButton(b);
        dh.display(null);
    }

    private void printout(String s) {
        System.out.println("[CLIENT_CV] " + s);
    }

    private void printerr(String s) {
        System.err.println("[CLIENT_CV] " + s);
    }

}
