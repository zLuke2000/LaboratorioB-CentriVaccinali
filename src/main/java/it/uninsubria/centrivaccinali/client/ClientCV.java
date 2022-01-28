package it.uninsubria.centrivaccinali.client;

import it.uninsubria.centrivaccinali.controller.Controller;
import it.uninsubria.centrivaccinali.controller.centri.CVLoginController;
import it.uninsubria.centrivaccinali.enumerator.TipologiaCentro;
import it.uninsubria.centrivaccinali.models.*;
import it.uninsubria.server.ServerCVInterface;
import it.uninsubria.centrivaccinali.util.DialogHelper;
import javafx.application.Platform;
import javafx.scene.control.Button;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Rappresenta il client dell'interfaccia utente.
 * @author ...
 */
public class ClientCV extends UnicastRemoteObject implements ClientCVInterface {


    /**
     * Varabile per identificare serial version RMI.
     */
    private static final long serialVersionUID = 1L;


    /**
     * Riferimento all'oggetto remoto server.
     * @see ServerCVInterface
     */
    private static ServerCVInterface server = null;


    /**
     * Thread di gestione della connessione.
     * @see ConnectionThread
     */
    private ConnectionThread connThread;


    /**
     * Riferimento alla classe astratta Controller.
     * @see Controller
     */
    private Controller controller;


    /**
     * Oggetto cittadino connesso al server dopo la login con successo.
     */
    private Cittadino cittadinoConnesso = null;


    /**
     * Nome del centro vaccinale corrispondente al cittadino connesso.
     */
    private String centroCittadino = "";


    /**
     * Costruttore oggetto ClientCV.
     * Crea un'istanza di un oggetto <code>ConnectionThread</code>.
     * @see ConnectionThread
     * @throws RemoteException eccezione rmi.
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
     * Imposta il riferimento al server a seguito dopo aver ottenuto la connessione.
     * @param server riferimento all'oggetto remoto server
     */
    public static void setServer(ServerCVInterface server) {
        ClientCV.server = server;
    }


    /**
     * Metodo per verificare la connessione con il server RMI.
     * @return true se la connessione è presente, false se la connessione risulta assente.
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
     * Metodo che notifica al client il completamento di un operazione richiesta al server.
     * @param ritorno oggetto che rappresenta il risultato dell'operazione.
     * @throws RemoteException eccezione rmi.
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
     * Metodo per effettuare la richiesta al server di registrazione del centro vaccinale.
     * @param controller controller dell'interfaccia da notificare.
     * @param cv centro vaccianale da registrare.
     */
    public void registraCentroVaccinale(Controller controller, CentroVaccinale cv) {
        this.controller = controller;
        try {
            server.registraCentro(this, cv);
        } catch (RemoteException e) {
            printerr("non e' stato possibile registrare il centro vaccinale");
            lanciaPopup();
        }
    }


    /**
     * Effettua una chiamata al server per registrare un nuovo cittadino.
     * @param controller il controller dell'interfaccia da notificare.
     * @param cittadino il cittadino da registrare.
     */
    public void registraCittadino(Controller controller, Cittadino cittadino) {
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
     * Effettua una chiamata al server per eseguire una operazione di login.
     * @param controller controller dell'interfaccia da notificare.
     * @param username nome utente del cittadino.
     * @param password password del cittadino.
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
     * Effettua una chiamata al server per eseguire una ricerca di centri vaccianali in base al nome.
     * @param controller controller dell'interfaccia da notificare.
     * @param nome nome del centro vaccinale da cercare.
     */
    public void ricercaPerNome(Controller controller, String nome) {
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
     * Effettua una chiamata al server per eseguire una ricerca di centri vaccianali in base al comune e alla tipologia.
     * @param controller controller dell'interfaccia da notificare.
     * @param comune comune da cercare.
     * @param tipologia tipologia di centro vaccinale da cercare.
     */
    public void ricercaPerComuneTipologia(Controller controller, String comune, TipologiaCentro tipologia) {
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
     * Effettua una chiamata al server per registrare un nuovo cittadino vaccinato.
     * @param controller controller dell'interfaccia da notificare.
     * @param vaccinato riferimento al vaccianato da registrare.
     */
    public void registraVaccinato(Controller controller, Vaccinato vaccinato) {
        this.controller = controller;
        try {
            server.registraVaccinato(this, vaccinato);
        } catch (RemoteException e) {
            printerr("registrazione vaccinato fallita");
            lanciaPopup();
        }
    }

    /**
     * Effettua una chiamata al server per ottenere la lista dei comuni in base alla provincia inserita nella ricerca.
     * @param controller controller dell'interfaccia da notificare.
     * @param provincia provincia da cercare.
     */
    public void getComuni(Controller controller, String provincia) {
        this.controller = controller;
        try {
            server.getComuni(this, provincia);
        } catch (RemoteException e) {
            printerr("impossibile effettuare la ricerca dei comuni");
            lanciaPopup();
        }
    }

    /**
     * Effettua una chiamata al server per ottenere la lista dei centriin base al comune inserito nella ricerca.
     * @param controller controller dell'interfaccia da notificare.
     * @param comune comune inserito dall'utente.
     */
    public void getCentri(Controller controller, String comune) {
        this.controller = controller;
        try {
            server.getCentri(this, comune);
        } catch (RemoteException e) {
            printerr("Impossibile effettuare la ricerca dei centri");
            lanciaPopup();
        }
    }

    /**
     * Effettua una chiamata al server per registrare un nuovo evento avverso.
     * @param controller controller dell'interfaccia da notificare.
     * @param ea evento avverso da registrare.
     */
    public void registraEventoAvverso(Controller controller , EventoAvverso ea) {
        this.controller = controller;
        try {
            server.registraEventoAvverso(this, ea);
        } catch (RemoteException e) {
            printerr("Impossibile registrare l'evento");
            lanciaPopup();
        }
    }

    /**
     * Effettua una chiamata al server per ottenere la media della severità per ogni evento avverso.
     * @param controller controller dell'interfaccia da notificare.
     * @param nomeCentro nome del centro per cui si vogliono ottenere i dati.
     */
    public void leggiEA(Controller controller, String nomeCentro) {
        this.controller = controller;
        try {
            server.leggiEA(this, nomeCentro);
        } catch (RemoteException e) {
            printerr("Impossibile recuperare gli eventi avversi per il centro");
            lanciaPopup();
        }
    }

    /**
     * Effettua una chiamata al server per leggere gli eventi avversi di un dato centro vaccinale.
     * @param controller controller dell'interfaccia da notificare.
     * @param nomeCentro nome del centro vaccinale di cui si vogliono leggere gli eventi.
     * @param limit numero di quanti eventi si vogliono leggere.
     * @param offset indice da cui partire a leggere gli eventi da db.
     */
    public void leggiSegnalazioni(Controller controller, String nomeCentro, int limit, int offset) {
        this.controller = controller;
        try {
            server.leggiSegnalazioni(this, nomeCentro, limit, offset);
        } catch (RemoteException e) {
            printerr("Impossibile recuperare gli eventi avversi per il centro");
            lanciaPopup();
        }
    }

    /**
     * Effettua una chiamata al server per richiedere l'annullamento dell'operazione in corso.
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
     * Effettua una chiamata al serever di eseguire la disconnessione, quindi la rimozione del riferiemto del client
     * dal suo registro RMI ed effettuare in seguito la chiusura dell'applicazione.
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
        Platform.runLater(Platform::exit);
    }

    /**
     * Effettua una chiamata al server per creare un popup con messaggio di errore di connesione al serever.
     * &amp;Egrave possibile scegliere se ritentare la connessione premendo "SI", altrimenti "NO".
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
     * Metodo per la stampa di una stringa di errore.
     * @param s stringa da stampare.
     */
    private void printerr(String s) {
        System.err.println("[CLIENT_CV] " + s);
    }

    /**
     * Effettua una chiamata al server per effettuare l'aggiornamento della password del'utente loggato.
     * @param controller controller dell'interfaccia da notificare.
     * @param userid parametro contenente lo user id dell'utente loggato.
     * @param vecchiaPassword parametro contenente la password corrente dell'utente loggato.
     * @param nuovaPassword parametro contenente la nuova password dell'utente loggato.
     */
    public void aggiornaPassword(Controller controller, String userid, String vecchiaPassword, String nuovaPassword) {
        this.controller = controller;
        try {
            server.aggiornaPassword(this, userid, vecchiaPassword, nuovaPassword);
        } catch (RemoteException e) {
            printerr("Impossibile aggiornare la password");
            lanciaPopup();
        }
    }
}
