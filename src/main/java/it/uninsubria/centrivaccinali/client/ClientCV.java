package it.uninsubria.centrivaccinali.client;

import it.uninsubria.centrivaccinali.controller.*;
import it.uninsubria.centrivaccinali.controller.centri.CVLoginController;
import it.uninsubria.centrivaccinali.controller.centri.CVRegistraCentroVaccinale;
import it.uninsubria.centrivaccinali.controller.centri.CVRegistraCittadinoController;
import it.uninsubria.centrivaccinali.controller.cittadini.dasboard.CIDashboardController;
import it.uninsubria.centrivaccinali.controller.cittadini.CIRegistrazioneController;
import it.uninsubria.centrivaccinali.controller.cittadini.dasboard.CIGraficiController;
import it.uninsubria.centrivaccinali.controller.cittadini.dasboard.CISegnalazioniController;
import it.uninsubria.centrivaccinali.controller.cittadini.dasboard.CIInfoCittadinoController;
import it.uninsubria.centrivaccinali.controller.cittadini.dasboard.AggiungiEventoAvverso;
import it.uninsubria.centrivaccinali.enumerator.TipologiaCentro;
import it.uninsubria.centrivaccinali.models.*;
import it.uninsubria.centrivaccinali.server.ServerCVInterface;
import it.uninsubria.centrivaccinali.util.DialogHelper;
import javafx.scene.control.Button;
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
            if (ritorno.getResult()) {
                cittadinoConnesso = ritorno.getCittadino();
                centroCittadino = ritorno.getCentroCittadino();
            }
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
        controller = ciDashboardController;
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
        controller = cvRegistraCittadinoController;
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

    public void registraEventoAvverso(AggiungiEventoAvverso eaController , EventoAvverso ea) {
        controller = eaController;
        try {
            server.registraEventoAvverso(this, ea);
        } catch (RemoteException e) {
            printerr("Impossibile registrare l'evento");
            lanciaPopup();
        }
    }

    public void leggiEA(CIGraficiController graficiController, String nomeCentro) {
        controller = graficiController;
        try {
            server.leggiEA(this, nomeCentro);
        } catch (RemoteException e) {
            printerr("Impossibile recuperare gli eventi avversi per il centro");
            lanciaPopup();
        }
    }

    public void leggiSegnalazioni(CISegnalazioniController segnalazioniController, String nomeCentro, int limit, int offset) {
        controller = segnalazioniController;
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
        dh.display(null);
    }

    private void printout(String s) {
        System.out.println("[CLIENT_CV] " + s);
    }

    private void printerr(String s) {
        System.err.println("[CLIENT_CV] " + s);
    }

    public void aggiornaPassword(CIInfoCittadinoController reference, String userid, String vecchiaPassword, String nuovaPassword) {
        controller = reference;
        try {
            server.aggiornaPassword(this, userid, vecchiaPassword, nuovaPassword);
        } catch (RemoteException e) {
            printerr("Impossibile aggiornare la password");
            lanciaPopup();
        }
    }
}
