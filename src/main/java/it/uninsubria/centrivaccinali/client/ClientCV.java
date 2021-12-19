package it.uninsubria.centrivaccinali.client;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.controller.*;
import it.uninsubria.centrivaccinali.controller.centri.CVLoginController;
import it.uninsubria.centrivaccinali.controller.centri.CVRegistraCentroVaccinale;
import it.uninsubria.centrivaccinali.controller.centri.CVRegistraCittadinoController;
import it.uninsubria.centrivaccinali.controller.cittadini.dashboard.*;
import it.uninsubria.centrivaccinali.controller.cittadini.CIRegistrazioneController;
import it.uninsubria.centrivaccinali.enumerator.TipologiaCentro;
import it.uninsubria.centrivaccinali.models.*;
import it.uninsubria.centrivaccinali.server.ServerCVInterface;
import it.uninsubria.centrivaccinali.util.DialogHelper;
import javafx.application.Platform;
import javafx.scene.control.Button;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 */
public class ClientCV extends UnicastRemoteObject implements ClientCVInterface {
    /**
     * Varabile per identificare serial version RMI.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Riferimento all'oggetto remoto server.
     */
    private static ServerCVInterface server = null;
    /**
     * @see ConnectionThread
     */
    private ConnectionThread connThread;
    /**
     * Riferimento classe astratta Controller.
     * @see Controller
     */
    private Controller controller;
    /**
     * Oggetto cittadino connesso al server dopo la login con successol.
     */
    private Cittadino cittadinoConnesso = null;
    /**
     * Nome del centro vaccinale corrispondente a <code>cittadinoConnesso</code>.
     */
    private String centroCittadino = "";

    /**
     * Costruttore oggetto ClientCV.
     * Crea un'istanza di un oggetto <code>ConnectionThread</code>.
     * @see ConnectionThread
     * @throws RemoteException
     */
    public ClientCV() throws RemoteException {
        connThread = new ConnectionThread();
    }

    /**
     * Metodo getter per dell'attributo <code>cittadinoConnesso</code>.
     * @return cittadino attualmente connesso.
     */
    public Cittadino getUtenteLoggato() {
        return cittadinoConnesso;
    }

    /**
     * Metodo getter per dell'attributo <code>centroCittadino</code>.
     * @return centro vaccinale di appartenenza del cittadino connesso.
     */
    public String getCentroCittadino() { return centroCittadino; }

    /**
     * Metodo per effettuare il logout dell'utente dall'applicazione
     */
    public void LogoutUtente() {
        this.cittadinoConnesso = null;
    }

    /**
     * Imposta il server nella classe attuale, previa avvenuta successione
     * @param server
     */
    public static void setServer(ServerCVInterface server) {
        ClientCV.server = server;
    }

    /**
     * Metodo per verificare la connessione con il server RMI.
     * @return true se la connessione è presente,
     * false se la connessione risulta assente.
     */
    private boolean connectionStatus() {
        if (server == null) {
            printerr("connessione al server assente");
            lanciaPopup();
            return false;
        }
        return true;
    }

    /**
     * Metodo che effettua l'autenticazione dell'operatore sanitario.
     * @param controller controller padre da cui arriva l'operazione, necessaria per la risposta.
     * @param username input dell'utente per effettuare autenticazione.
     * @param password input dell'utente per effettuare autenticazione.
     */
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

    /**
     *
     * @param ritorno
     * @throws RemoteException
     */
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

    /**
     *
     * @param controller
     * @param cv
     */
    public void registraCentroVaccinale(CVRegistraCentroVaccinale controller, CentroVaccinale cv) {
        this.controller = controller;
        try {
            server.registraCentro(this, cv);
        } catch (RemoteException e) {
            printerr("non e' stato possibile registrare il centro vaccinale");
            lanciaPopup();
        }
    }

    /**
     *
     * @param controller
     * @param cittadino
     */
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

    /**
     *
     * @param controller
     * @param username
     * @param password
     */
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

    /**
     *
     * @param controller
     * @param nome
     */
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

    /**
     *
     * @param controller
     * @param comune
     * @param tipologia
     */
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

    /**
     *
     * @param controller
     * @param vaccinato
     */
    public void registraVaccinato(CVRegistraCittadinoController controller, Vaccinato vaccinato) {
        this.controller = controller;
        try {
            server.registraVaccinato(this, vaccinato);
        } catch (RemoteException e) {
            printerr("registrazione vaccinato fallita");
            lanciaPopup();
        }
    }

    /**
     *
     * @param controller
     * @param provincia
     */
    public void getComuni(CVRegistraCittadinoController controller, String provincia) {
        this.controller = controller;
        try {
            server.getComuni(this, provincia);
        } catch (RemoteException e) {
            printerr("impossibile effettuare la ricerca dei comuni");
            lanciaPopup();
        }
    }

    /**
     *
     * @param controller
     * @param comune
     */
    public void getCentri(CVRegistraCittadinoController controller, String comune) {
        this.controller = controller;
        try {
            server.getCentri(this, comune);
        } catch (RemoteException e) {
            printerr("Impossibile effettuare la ricerca dei centri");
            lanciaPopup();
        }
    }

    /**
     *
     * @param controller
     * @param ea
     */
    public void registraEventoAvverso(AggiungiEventoAvverso controller , EventoAvverso ea) {
        this.controller = controller;
        try {
            server.registraEventoAvverso(this, ea);
        } catch (RemoteException e) {
            printerr("Impossibile registrare l'evento");
            lanciaPopup();
        }
    }

    /**
     *
     * @param controller
     * @param nomeCentro
     */
    public void leggiEA(CIGraficiController controller, String nomeCentro) {
        this.controller = controller;
        try {
            server.leggiEA(this, nomeCentro);
        } catch (RemoteException e) {
            printerr("Impossibile recuperare gli eventi avversi per il centro");
            lanciaPopup();
        }
    }

    /**
     *
     * @param controller
     * @param nomeCentro
     * @param limit
     * @param offset
     */
    public void leggiSegnalazioni(CISegnalazioniController controller, String nomeCentro, int limit, int offset) {
        this.controller = controller;
        try {
            server.leggiSegnalazioni(this, nomeCentro, limit, offset);
        } catch (RemoteException e) {
            printerr("Impossibile recuperare gli eventi avversi per il centro");
            lanciaPopup();
        }
    }

    /**
     *
     */
    public void stopOperation() {
        if (server != null) {
            try {
                server.stopThread();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     */
    public void disconnetti() {
        if (connThread.isAlive()){
            connThread.interrupt();
        }
        try {
            UnicastRemoteObject.unexportObject(this, true);
        } catch (NoSuchObjectException e) {
            e.printStackTrace();
        }
        Platform.runLater(() -> {
            CentriVaccinali.stage.close();
            Platform.exit();
        });

    }

    /**
     *
     */
    private void lanciaPopup() {
        DialogHelper dh = new DialogHelper("ERRORE DI CONNESSIONE", "L'applicazione non e' attualmente connessa al server \n Vuoi provare a connetterti?", DialogHelper.Type.ERROR);
        Button b = new Button("SI");
        b.setOnAction( eh -> {
            if (connThread.isAlive()){
                connThread.stopThread();
            }
            connThread = new ConnectionThread();
            dh.close();
        });
        dh.addButton(b);
        dh.display();
    }

    /**
     *
     * @param s
     */
    private void printout(String s) {
        System.out.println("[CLIENT_CV] " + s);
    }

    /**
     *
     * @param s
     */
    private void printerr(String s) {
        System.err.println("[CLIENT_CV] " + s);
    }

    /**
     *
     * @param controller
     * @param userid
     * @param vecchiaPassword
     * @param nuovaPassword
     */
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
