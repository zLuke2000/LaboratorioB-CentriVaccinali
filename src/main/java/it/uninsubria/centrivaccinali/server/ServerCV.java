package it.uninsubria.centrivaccinali.server;

import it.uninsubria.centrivaccinali.client.ClientCVInterface;
import it.uninsubria.centrivaccinali.database.Database;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.Cittadino;
import it.uninsubria.centrivaccinali.models.Result;
import it.uninsubria.centrivaccinali.models.Vaccinato;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerCV extends UnicastRemoteObject implements ServerCVInterface{

    private static final long serialVersionUID = 1L;
    private final String usernameOperatore = "123";
    private final String passwordOperatore = "123";
    private static Database db;
    private Thread myThread;
    private static Registry reg;
    private static ServerCV obj;

    protected ServerCV() throws RemoteException {
    }

    public static void main(String[] args) {
        System.out.println("Inserire le credenziali di accesso");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // Leggo nome utente
        // Da sistemare
        String utente = "123abc";
        /*
        System.out.print("utente: ");
        try {
            utente = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
         */

        // Leggo password
        // Da sistemare
        String password = "123abc";
        /*
        System.out.print("password: ");
        try {
            password = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
         */

        db = new Database();
        if(db.connect(utente, password)) {
            try {
                obj = new ServerCV();
                reg = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
                reg.rebind("server", obj);
                System.out.println("Server pronto");
            } catch (RemoteException e) {
                System.err.println("[SERVER_CV] Errore durante la pubblicazione del server sul registro RMI");
                e.printStackTrace();
                System.exit(-1);
            }
            db.getCentriVaccinali("");
        }
    }

    @Override
    public void authOperatore(ClientCVInterface client, String username, String password) throws RemoteException {
        myThread = new Thread(() -> {
            try {
                // simulazione attesa
                Thread.sleep(5000);
                client.notifyStatus(new Result(usernameOperatore.equals(username) && passwordOperatore.equals(password), Result.LOGIN_OPERATORE));
            } catch (InterruptedException | RemoteException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public int registraCentro(CentroVaccinale cv) throws RemoteException {
        return db.registraCentroVaccinale(cv);
    }

    @Override
    public void registraCittadino(ClientCVInterface client, Cittadino cittadino) {
        myThread = new Thread(() -> {
            try {
                client.notifyStatus(db.registraCittadino(cittadino));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        myThread.start();
    }

    @Override
    public void registraVaccinato(ClientCVInterface client, Vaccinato vaccinato) throws RemoteException {
        myThread = new Thread(() -> {
            try {
                client.notifyStatus(db.registraVaccinato( vaccinato));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        myThread.start();
    }

    @Override
    public void loginUtente(ClientCVInterface client, String username, String password) throws RemoteException {
        myThread = new Thread(() -> {
            try {
                client.notifyStatus(db.loginUtente(username, password));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        myThread.start();
    }

    @Override
    public void getCentri(ClientCVInterface client, String comuni) {
        myThread = new Thread(() -> {
            try {
                client.notifyStatus(db.getCentriVaccinali(comuni));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void getComuni(ClientCVInterface client, String provincia) {
        myThread = new Thread(() -> {
            try {
                client.notifyStatus(db.getComuni(provincia));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void stopThread() {
        myThread.interrupt();
    }
}
