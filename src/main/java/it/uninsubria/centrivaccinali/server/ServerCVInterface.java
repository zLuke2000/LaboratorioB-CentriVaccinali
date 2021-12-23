package it.uninsubria.centrivaccinali.server;

import it.uninsubria.centrivaccinali.client.ClientCVInterface;
import it.uninsubria.centrivaccinali.enumerator.TipologiaCentro;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.Cittadino;
import it.uninsubria.centrivaccinali.models.EventoAvverso;
import it.uninsubria.centrivaccinali.models.Vaccinato;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaccia dell'oggetto remoto <code>ServerCV</code>.
 * @see ServerCV
 */
public interface ServerCVInterface extends Remote{

    // Metodi operatore sanitario
    /**
     * Permette di autenticare un operatore sanitario.
     * @param client riferimento al client da notificare.
     * @param username nome utente inserito.
     * @param password password inserita.
     * @throws RemoteException eccezione rmi.
     */
    void authOperatore(ClientCVInterface client, String username, String password) throws RemoteException;

    /**
     * Permette di registrare un nuovo centro vaccinale.
     * @param client riferimento al client da notificare.
     * @param cv centro vaccinale da registrare.
     * @throws RemoteException eccezione rmi.
     */
    void registraCentro(ClientCVInterface client, CentroVaccinale cv) throws RemoteException;

    /**
     * Permette di registrare un nuovo vaccinato.
     * @param client riferimento al client da notificare.
     * @param vaccinato nuovo vaccinato da registrare.
     * @throws RemoteException eccezione rmi.
     */
    void registraVaccinato(ClientCVInterface client, Vaccinato vaccinato) throws RemoteException;

    // Metodi cittadino
    /**
     * Permette di registrare un nuovo cittadino.
     * @param client riferimento al client da notificare.
     * @param cittadino cittadino da registrare.
     * @throws RemoteException eccezione rmi.
     */
    void registraCittadino(ClientCVInterface client, Cittadino cittadino) throws RemoteException;

    /**
     * Effettua una operazione di login.
     * @param client riferimento al client da notificare.
     * @param username nome utente inserito dal cittadino.
     * @param password password inserita dal cittadino.
     * @throws RemoteException eccezione rmi.
     */
    void loginUtente(ClientCVInterface client, String username, String password) throws RemoteException;

    /**
     * Effettua la ricerca di un centro vaccinale in base al nome.
     * @param client riferimento al client da notificare.
     * @param nomeCentro nome del centro vaccinale da cercare.
     * @throws RemoteException eccezione rmi.
     */
    void ricercaCentroPerNome(ClientCVInterface client, String nomeCentro) throws RemoteException;

    /**
     * Effettua la ricerca di un centro vaccinale in base al comune e alla tipologia.
     * @param client riferimento al client da notificare.
     * @param comune comune del centro vaccinale che si vuole cercare.
     * @param tipologia tipologia del centro vaccinale da cercare.
     * @throws RemoteException eccezione rmi.
     */
    void ricercaCentroPerComuneTipologia(ClientCVInterface client, String comune, TipologiaCentro tipologia) throws RemoteException;

    /**
     * Permette di registrare un nuovo evento avverso.
     * @param client riferimento al client da notificare.
     * @param ea evento avverso da registrare.
     * @throws RemoteException eccezione rmi.
     */
    void registraEventoAvverso(ClientCVInterface client, EventoAvverso ea) throws RemoteException;

    /**
     * Permette di modificare la password del cittadino.
     * @param client riferimento al client da notificare.
     * @param userid nome utente del cittadino.
     * @param vecchiaPassword vecchia password del cittadino.
     * @param nuovaPassword nuova password del cittadino.
     * @throws RemoteException eccezione rmi.
     */
    void aggiornaPassword(ClientCVInterface client, String userid, String vecchiaPassword, String nuovaPassword) throws RemoteException;

    /**
     * Recupera la media della severit&amp;agrave per ogni tipo di evento avverso registrato.
     * @param client riferimento al client da notificare.
     * @param nomeCentro centro da cui leggere i valori.
     * @throws RemoteException eccezione rmi.
     */
    void leggiEA(ClientCVInterface client, String nomeCentro) throws  RemoteException;

    /**
     * Leggi gli eventi avversi di un dato centro vaccinale.
     * @param client riferimento al client da notificare.
     * @param nomeCentro centro da cui leggere gli eventi.
     * @param limit numero di eventi che si vogliono leggere.
     * @param offset indice da cui iniziare a leggere gli eventi da database.
     * @throws RemoteException eccezione rmi.
     */
    void leggiSegnalazioni(ClientCVInterface client, String nomeCentro, int limit, int offset) throws RemoteException;

    // Metodi in comune
    /**
     * Recupera la lista dei comuni in base alla provincia inserita dall'utente.
     * @param client riferimento al client da notificare.
     * @param provincia provincia inserita dall'utente.
     * @throws RemoteException eccezione rmi.
     */
    void getComuni(ClientCVInterface client, String provincia) throws RemoteException;

    /**
     * Recupera la lista dei centri vaccinali in base al comune inserito dall'utente.
     * @param client riferimento al client da notificare.
     * @param comune comune inserito dall'utente.
     * @throws RemoteException eccezione rmi.
     */
    void getCentri(ClientCVInterface client, String comune) throws RemoteException;

    /**
     * Permette di annullare l'operazione in corso.
     * @throws RemoteException eccezione rmi.
     */
    void stopThread() throws RemoteException;
}
