//Centore Luca 740951 VA
//Lattarulo Luca 742597 VA
//Marelli Samuele 742495 VA
//Pintonello Christian 741112 VA
package it.uninsubria.server;

import it.uninsubria.centrivaccinali.client.ClientCVInterface;
import it.uninsubria.server.database.Database;
import it.uninsubria.centrivaccinali.enumerator.TipologiaCentro;
import it.uninsubria.centrivaccinali.models.*;

import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Classe che rappresenta il server remoto.
 * @author Centore Luca 740951
 * @author Lattarulo Luca 742597
 * @author Marelli Samuele 742495
 * @author Pintonello Christian 741112
 */
public class ServerCV extends UnicastRemoteObject implements ServerCVInterface {

    /**
     * Varabile per identificare serial version RMI.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Campo username operatore sanitario.
     */
    private final String usernameOperatore = "admin";

    /**
     * Campo password operatore sanitario.
     */
    private final String passwordOperatore = "admin";

    /**
     * Riferimento al database.
     * @see Database
     */
    private static Database db;

    /**
     * Thread che gestisce l'operazione richiesta dal client.
     */
    private Thread myThread;

    /**
     * Costruttore vuoto per la classe <code>ServerCV</code>
     * @throws RemoteException eccezione rmi.
     */
    protected ServerCV() throws RemoteException { }


    /**
     * Metodo main, crea l'oggetto server e lo mette in ascolto sul <code>Registry</code>
     * @param args args param
     */
    public static void main(String[] args) {
        db = new Database();
        if(db.connettiDB()) {
            try {
                ServerCV obj = new ServerCV();
                Registry reg = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
                reg.rebind("server", obj);
                System.out.println("Server pronto: " + reg);
            } catch (RemoteException e) {
                System.err.println("[SERVER_CV] Errore durante la pubblicazione del server sul registro RMI");
                System.exit(-1);
            }
        }
    }


    /**
     * Crea un thread che richiede al database di autenticare l'operatore sanitario.
     * @param client riferimento al client da notificare.
     * @param username nome utente inserito.
     * @param password password inserita.
     * @throws RemoteException eccezione rmi.
     */
    @Override
    public synchronized void authOperatore(ClientCVInterface client, String username, String password) throws RemoteException {
        myThread = new Thread(() -> {
            try {
                client.notifyStatus(new Result(usernameOperatore.equals(username) && passwordOperatore.equals(password), Result.Operation.LOGIN_OPERATORE));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        myThread.start();
    }


    /**
     * Crea un thread che richiede al database di registrare un nuovo centro vaccinale.
     * @param client riferimento al client da notificare.
     * @param cv centro vaccinale da registrare.
     * @throws RemoteException eccezione rmi.
     */
    @Override
    public synchronized void registraCentro(ClientCVInterface client, CentroVaccinale cv) throws RemoteException {
        myThread = new Thread(() -> {
            try {
                client.notifyStatus(db.registraCentroVaccinale(cv));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        myThread.start();
    }


    /**
     * Crea un thread che richiede al database di registrare un nuovo cittadino.
     * @param client riferimento al client da notificare.
     * @param cittadino cittadino da registrare.
     */
    @Override
    public synchronized void registraCittadino(ClientCVInterface client, Cittadino cittadino) {
        myThread = new Thread(() -> {
            try {
                client.notifyStatus(db.registraCittadino(cittadino));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        myThread.start();
    }


    /**
     * Crea un thread che richiede al database di registrare un nuovo vaccinato.
     * @param client riferimento al client da notificare.
     * @param vaccinato vaccinato da registrare.
     * @throws RemoteException eccezione rmi.
     */
    @Override
    public synchronized void registraVaccinato(ClientCVInterface client, Vaccinato vaccinato) throws RemoteException {
        myThread = new Thread(() -> {
            try {
                client.notifyStatus(db.registraVaccinato(vaccinato));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        myThread.start();
    }


    /**
     * Crea un thread che richiede al database di effettuare una operazione di login
     * @param client riferimento al client da notificare.
     * @param username nome utente inserito dall'utente.
     * @param password password inserita dall'utente.
     * @throws RemoteException eccezione rmi.
     */
    @Override
    public synchronized void loginUtente(ClientCVInterface client, String username, String password) throws RemoteException {
        myThread = new Thread(() -> {
            try {
                client.notifyStatus(db.loginUtente(username, password));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        myThread.start();
    }


    /**
     * Crea un thread che richiede al database cercare un centro vaccinale in base al nome inserito.
     * @param client riferimento al client da notificare.
     * @param nomeCentro nome del centro da cercare.
     * @throws RemoteException eccezione rmi.
     */
    @Override
    public synchronized void ricercaCentroPerNome(ClientCVInterface client, String nomeCentro) throws RemoteException {
        myThread = new Thread(() -> {
            try {
                client.notifyStatus(db.ricercaCentroPerNome(nomeCentro));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        myThread.start();
    }


    /**
     * Crea un thread che richiede al database di cercare un centro vaccinale in base al comune inserito e alla tipologia scelta.
     * @param client riferimento al client da notificare.
     * @param comune comune del centro da cercare
     * @param tipologia tipologia del centro da cercare.
     */
    @Override
    public synchronized void ricercaCentroPerComuneTipologia(ClientCVInterface client, String comune, TipologiaCentro tipologia) {
        myThread = new Thread(() -> {
            try {
                client.notifyStatus(db.ricercaCentroPerComuneTipologia(comune, tipologia));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        myThread.start();
    }


    /**
     * Crea un thread che richiede al database di registrare un nuovo evento avverso.
     * @param client riferimento al client da notificare.
     * @param ea evento avverso da registrare.
     */
    @Override
    public synchronized void registraEventoAvverso(ClientCVInterface client, EventoAvverso ea) {
        myThread = new Thread(() -> {
            try {
                client.notifyStatus(db.registraEA(ea));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        myThread.start();
    }


    /**
     * Crea un thread che richiede al database di aggiornare la password dell'utente.
     * @param client riferimento al client da notificare.
     * @param userid nome utente del cittadino che vuole aggiornare la propria password.
     * @param vecchiaPassword vecchia password da aggiornare.
     * @param nuovaPassword nuova password da salvare.
     */
    @Override
    public synchronized void aggiornaPassword(ClientCVInterface client, String userid, String vecchiaPassword, String nuovaPassword) {
        myThread = new Thread(() -> {
            try {
                client.notifyStatus(db.aggiornaPSW(userid, vecchiaPassword, nuovaPassword));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        myThread.start();
    }


    /**
     * Crea un thread che richiede al database di ottenere il valore medio per ogni tipo di evento avverso, registrato in un dato centro.
     * @param client riferimento al client da notificare.
     * @param nomeCentro centro vaccinale da cui leggere i valori.
     * @throws RemoteException eccezione rmi.
     */
    @Override
    public synchronized void leggiEA(ClientCVInterface client, String nomeCentro) throws RemoteException {
        myThread = new Thread(() -> {
            try {
                client.notifyStatus(db.leggiMediaEventiAvversi(nomeCentro));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        myThread.start();
    }


    /**
     * Crea un thread che richiede al database di leggere gli eventi avversi registrati in un dato centro.
     * @param client riferimento al client da notificare.
     * @param nomeCentro centro vaccinale da cui si vuole leggere gli eventi avversi.
     * @param limit valore che indica quanti eventi si vogliono leggere.
     * @param offset indice da cui iniziare a leggere gli eventi da database.
     * @throws RemoteException eccezione rmi.
     */
    @Override
    public synchronized void leggiSegnalazioni(ClientCVInterface client, String nomeCentro, int limit, int offset) throws RemoteException {
        myThread = new Thread(() -> {
            try {
                client.notifyStatus(db.leggiSegnalazioni(nomeCentro, limit, offset));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        myThread.start();
    }


    /**
     * Crea un thread che richiede al database di ottenere la lista dei centri in base al comune inserito.
     * @param client riferimento al client da notificare.
     * @param comune comune inserito dall'utente
     */
    @Override
    public synchronized void getCentri(ClientCVInterface client, String comune) {
        myThread = new Thread(() -> {
            try {
                client.notifyStatus(db.getCentriVaccinali(comune));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        myThread.start();
    }


    /**
     * Crea un thread che richiede al database di ottenere la lista dei comuni in base alla provincia inserit.
     * @param client riferimento al client da notificare.
     * @param provincia provincia inserita.
     */
    @Override
    public synchronized void getComuni(ClientCVInterface client, String provincia) {
        myThread = new Thread(() -> {
            try {
                client.notifyStatus(db.getComuni(provincia));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        myThread.start();
    }


    /**
     * Metodo per interrompere l'operazione in corso, se presente.
     */
    @Override
    public synchronized void stopThread() {
        if (myThread != null) {
            myThread.interrupt();
        }
    }
}
