package it.uninsubria.centrivaccinali.client;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.controller.*;
import it.uninsubria.centrivaccinali.controller.centri.CVLoginController;
import it.uninsubria.centrivaccinali.controller.centri.CVRegistraCentroVaccinale;
import it.uninsubria.centrivaccinali.controller.centri.CVRegistraCittadinoController;
import it.uninsubria.centrivaccinali.controller.cittadini.dasboard.*;
import it.uninsubria.centrivaccinali.controller.cittadini.CIRegistrazioneController;
import it.uninsubria.centrivaccinali.enumerator.TipologiaCentro;
import it.uninsubria.centrivaccinali.models.*;
import it.uninsubria.centrivaccinali.server.ServerCVInterface;
import it.uninsubria.centrivaccinali.util.DialogHelper;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientCV extends UnicastRemoteObject implements ClientCVInterface {

    private static final long serialVersionUID = 1L;
    private static ServerCVInterface server = null;
    private Cittadino cittadinoConnesso = null;
    private ConnectionThread connThread;
    private String centroCittadino = "";
    private Controller controller;

    public ClientCV() throws RemoteException {
        //si occupa il thread di ottenere la connessione
        connThread = new ConnectionThread();
    }

    public Cittadino getUtenteLoggato() {
        return cittadinoConnesso;
    }
    public String getCentroCittadino() { return centroCittadino; }

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

    public void autenticaOperatore(CVLoginController controller, String username, String password) {
        this.controller = controller;
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
            if (ritorno.getResult()) {
                cittadinoConnesso = ritorno.getCittadino();
                centroCittadino = ritorno.getCentroCittadino();
            }
        }
        controller.notifyController(ritorno);
    }

    public void registraCentroVaccinale(CVRegistraCentroVaccinale controller, CentroVaccinale cv) {
        this.controller = controller;
        try {
            server.registraCentro(this, cv);
        } catch (RemoteException e) {
            printerr("non e' stato possibile registrare il centro vaccinale");
            lanciaPopup();
        }
    }

    public void registraCittadino(CIRegistrazioneController controller, Cittadino cittadino) {
        this.controller = controller;
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

    public void ricercaPerNome(CIRicercaResultController controller, String nome) {
        this.controller = controller;
        if (connectionStatus()) {
            try {
                server.ricercaCentroPerNome(this, nome);
            } catch (RemoteException e) {
                printerr("ricerca fallita");
                lanciaPopup();
            }
        }
    }

    public void ricercaPerComuneTipologia(CIRicercaResultController controller, String comune, TipologiaCentro tipologia) {
        this.controller = controller;
        if (connectionStatus()) {
            try {
                server.ricercaCentroPerComuneTipologia(this, comune, tipologia);
            } catch (RemoteException e) {
                printerr("ricerca fallita");
                lanciaPopup();
            }
        }
    }

    public void registraVaccinato(CVRegistraCittadinoController controller, Vaccinato vaccinato) {
        this.controller = controller;
        try {
            server.registraVaccinato(this, vaccinato);
        } catch (RemoteException e) {
            printerr("registrazione vaccinato fallita");
            lanciaPopup();
        }
    }

    public void getComuni(CVRegistraCittadinoController controller, String provincia) {
        this.controller = controller;
        try {
            server.getComuni(this, provincia);
        } catch (RemoteException e) {
            printerr("impossibile effettuare la ricerca dei comuni");
            lanciaPopup();
        }
    }

    public void getCentri(CVRegistraCittadinoController controller, String comune) {
        this.controller = controller;
        try {
            server.getCentri(this, comune);
        } catch (RemoteException e) {
            printerr("Impossibile effettuare la ricerca dei centri");
            lanciaPopup();
        }
    }

    public void registraEventoAvverso(AggiungiEventoAvverso controller , EventoAvverso ea) {
        this.controller = controller;
        try {
            server.registraEventoAvverso(this, ea);
        } catch (RemoteException e) {
            printerr("Impossibile registrare l'evento");
            lanciaPopup();
        }
    }

    public void leggiEA(CIGraficiController controller, String nomeCentro) {
        this.controller = controller;
        try {
            server.leggiEA(this, nomeCentro);
        } catch (RemoteException e) {
            printerr("Impossibile recuperare gli eventi avversi per il centro");
            lanciaPopup();
        }
    }

    public void leggiSegnalazioni(CISegnalazioniController controller, String nomeCentro, int limit, int offset) {
        this.controller = controller;
        try {
            server.leggiSegnalazioni(this, nomeCentro, limit, offset);
        } catch (RemoteException e) {
            printerr("Impossibile recuperare gli eventi avversi per il centro");
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
            //FIXME non chiude interfaccia
            if (connThread.isAlive()){
                connThread.interrupt();
            }
            connThread = new ConnectionThread();
            ((Stage)((Button) eh.getSource()).getScene().getWindow()).close();
        });
        dh.addButton(b);
        dh.display((Pane) CentriVaccinali.scene.getRoot());
    }

    private void printout(String s) {
        System.out.println("[CLIENT_CV] " + s);
    }

    private void printerr(String s) {
        System.err.println("[CLIENT_CV] " + s);
    }

    public void aggiornaPassword(CIInfoCittadinoController controller, String userid, String vecchiaPassword, String nuovaPassword) {
        this.controller = controller;
        try {
            server.aggiornaPassword(this, userid, vecchiaPassword, nuovaPassword);
        } catch (RemoteException e) {
            printerr("Impossibile aggiornare la password");
            lanciaPopup();
        }
    }
}
