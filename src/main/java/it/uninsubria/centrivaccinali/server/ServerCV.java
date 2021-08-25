package it.uninsubria.centrivaccinali.server;

import it.uninsubria.centrivaccinali.database.Database;
import it.uninsubria.centrivaccinali.models.Vaccinato;

import java.io.BufferedReader;
import java.io.IOException;
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
    private static Registry reg;
    private static ServerCV obj;

    protected ServerCV() throws RemoteException {
    }

    public static void main(String[] args) {
        System.out.println("Inserire le credenziali di accesso");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // Leggo nome utente
        String utente = null;
        System.out.print("utente: ");
        try {
            utente = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Leggo password
        String password = null;
        System.out.print("password: ");
        try {
            password = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        db = new Database();
        if(db.connect(utente, password)) {
            try {
                obj = new ServerCV();
                reg = LocateRegistry.createRegistry(1099);
                reg.rebind("server", obj);
                System.out.println("Server pronto");
            } catch (RemoteException e) {
                System.err.println("[SERVER_CV] Errore durante la pubblicazione del server sul registro RMI");
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }

    @Override
    public boolean autenticaOperatore(String username, String password) throws RemoteException {
        System.out.println("[SERVER] richiesta di autenticazione da parte di: " + username);
        return usernameOperatore.equals(username) && passwordOperatore.equals(password);
    }
}
