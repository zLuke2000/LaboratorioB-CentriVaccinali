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
 *
 */
public interface ServerCVInterface extends Remote{
    // Metodi operatore sanitario

    /**
     *
     * @param client
     * @param username
     * @param password
     * @throws RemoteException
     */
    void authOperatore(ClientCVInterface client, String username, String password) throws RemoteException;

    /**
     *
     * @param client
     * @param cv
     * @throws RemoteException
     */
    void registraCentro(ClientCVInterface client, CentroVaccinale cv) throws RemoteException;

    /**
     *
     * @param client
     * @param cittadino
     * @throws RemoteException
     */
    void registraCittadino(ClientCVInterface client, Cittadino cittadino) throws RemoteException;
    // Metodi cittadino

    /**
     *
     * @param client
     * @param vaccinato
     * @throws RemoteException
     */
    void registraVaccinato(ClientCVInterface client, Vaccinato vaccinato) throws RemoteException;

    /**
     *
     * @param client
     * @param username
     * @param password
     * @throws RemoteException
     */
    void loginUtente(ClientCVInterface client, String username, String password) throws RemoteException;

    /**
     *
     * @param client
     * @param nomeCentro
     * @throws RemoteException
     */
    void ricercaCentroPerNome(ClientCVInterface client, String nomeCentro) throws RemoteException;

    /**
     *
     * @param clientCV
     * @param comune
     * @param tipologia
     * @throws RemoteException
     */
    void ricercaCentroPerComuneTipologia(ClientCVInterface clientCV, String comune, TipologiaCentro tipologia) throws RemoteException;

    /**
     *
     * @param clientCV
     * @param ea
     * @throws RemoteException
     */
    void registraEventoAvverso(ClientCVInterface clientCV, EventoAvverso ea) throws RemoteException;

    /**
     *
     * @param clientCV
     * @param userid
     * @param vecchiaPassword
     * @param nuovaPassword
     * @throws RemoteException
     */
    void aggiornaPassword(ClientCVInterface clientCV, String userid, String vecchiaPassword, String nuovaPassword) throws RemoteException;

    /**
     *
      * @param client
     * @param nomeCentro
     * @throws RemoteException
     */
    void leggiEA(ClientCVInterface client, String nomeCentro) throws  RemoteException;

    /**
     *
     * @param client
     * @param nomeCentro
     * @param limit
     * @param offset
     * @throws RemoteException
     */
    void leggiSegnalazioni(ClientCVInterface client, String nomeCentro, int limit, int offset) throws RemoteException;
    // Metodi in comune

    /**
     *
     * @param client
     * @param provincia
     * @throws RemoteException
     */
    void getComuni(ClientCVInterface client, String provincia) throws RemoteException;

    /**
     *
     * @param client
     * @param comune
     * @throws RemoteException
     */
    void getCentri(ClientCVInterface client, String comune) throws RemoteException;

    /**
     *
     * @throws RemoteException
     */
    void stopThread() throws RemoteException;
}
