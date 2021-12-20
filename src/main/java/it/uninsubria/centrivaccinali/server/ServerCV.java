package it.uninsubria.centrivaccinali.server;

import it.uninsubria.centrivaccinali.client.ClientCVInterface;
import it.uninsubria.centrivaccinali.database.Database;
import it.uninsubria.centrivaccinali.enumerator.TipologiaCentro;
import it.uninsubria.centrivaccinali.models.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerCV extends UnicastRemoteObject implements ServerCVInterface {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    private final String usernameOperatore = "123";
    /**
     *
     */
    private final String passwordOperatore = "123";
    /**
     *
     */
    private static Database db;
    /**
     *
     */
    private Thread myThread;

    /**
     *
     * @throws RemoteException
     */
    protected ServerCV() throws RemoteException {
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        db = new Database();
        try {
            ServerCV obj = new ServerCV();
            Registry reg = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            reg.rebind("server", obj);
            System.out.println("Server pronto: " + reg);
        } catch (RemoteException e) {
            System.err.println("[SERVER_CV] Errore durante la pubblicazione del server sul registro RMI");
            e.printStackTrace();
            System.exit(-1);
        }
        db.getCentriVaccinali("");
    }

    /**
     *
     * @param client
     * @param username
     * @param password
     * @throws RemoteException
     */
    @Override
    public synchronized void authOperatore(ClientCVInterface client, String username, String password) throws RemoteException {
        myThread = new Thread(() -> {
            try {
                // simulazione attesa
                Thread.sleep(1000);
                client.notifyStatus(new Result(usernameOperatore.equals(username) && passwordOperatore.equals(password), Result.Operation.LOGIN_OPERATORE));
            } catch (InterruptedException | RemoteException e) {
                e.printStackTrace();
            }
        });
        myThread.start();
    }

    /**
     *
     * @param client
     * @param cv
     * @throws RemoteException
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
     *
     * @param client
     * @param cittadino
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
     *
     * @param client
     * @param vaccinato
     * @throws RemoteException
     */
    @Override
    public synchronized void registraVaccinato(ClientCVInterface client, Vaccinato vaccinato) throws RemoteException {
        myThread = new Thread(() -> {
            try {
                client.notifyStatus(db.registraVaccinato( vaccinato));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        myThread.start();
    }

    /**
     *
     * @param client
     * @param username
     * @param password
     * @throws RemoteException
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
     *
     * @param client
     * @param nomeCentro
     * @throws RemoteException
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
     *
     * @param client
     * @param comune
     * @param tipologia
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
     *
     * @param client
     * @param ea
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
     *
     * @param client
     * @param userid
     * @param vecchiaPassword
     * @param nuovaPassword
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
     *
     * @param client
     * @param nomeCentro
     * @throws RemoteException
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
     *
     * @param client
     * @param nomeCentro
     * @param limit
     * @param offset
     * @throws RemoteException
     */
    @Override
    public void leggiSegnalazioni(ClientCVInterface client, String nomeCentro, int limit, int offset) throws RemoteException {
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
     *
     * @param client
     * @param comuni
     */
    @Override
    public synchronized void getCentri(ClientCVInterface client, String comuni) {
        myThread = new Thread(() -> {
            try {
                client.notifyStatus(db.getCentriVaccinali(comuni));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        myThread.start();
    }

    /**
     *
     * @param client
     * @param provincia
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
     *
     */
    @Override
    public synchronized void stopThread() {
        if (myThread != null) {
            myThread.interrupt();
        }
    }
}
