package it.uninsubria.centrivaccinali.models;

import java.io.Serializable;

/**
 * @author Centore Luca 740951
 * @author Lattarulo Luca 742597
 * @author Marelli Samuele
 * @author Pintonello Christian 741112
 *
 * Classe per salvare le informazioni del cittadino registrato
 */
public class Cittadino implements Serializable {
    private static final long serialVersionUID = 1L;

    /**Rappresenta il nome del cittadino*/
    private String nome;
    /**Rappresenta il cognome del cittadino*/
    private String cognome;
    /**Rappresenta il codice fiscale del cittadino*/
    private String codicefiscale;
    /**Rappresenta la mail del cittadino*/
    private String mail;
    /**Rappresenta l'userid con cui si registra il cittadino*/
    private String userid;
    /**Rappresenta la password dell'account per il cittadino*/
    private String password;
    /**Rappresenta l'id ricevuto al momento della vaccinazione*/
    private long id_vaccino;

    public Cittadino() {}

    /**
     *Costruttore primario per Cittadino
     * @param nome nome del cittadino
     * @param cognome cognome del cittadino
     * @param codicefiscale codice fiscale del cittadino
     * @param mail mail del cittadino
     * @param userid userid di registrazione del cittadino
     * @param password password del cittadino
     * @param id_vaccino id di vaccinazione del cittadino
     */
    public Cittadino(String nome, String cognome, String codicefiscale, String mail, String userid, String password, long id_vaccino) {
        this.nome = nome;
        this.cognome = cognome;
        this.codicefiscale = codicefiscale;
        this.mail = mail;
        this.userid = userid;
        this.password = password;
        this.id_vaccino = id_vaccino;
    }

    /**
     * Metodo getter per il nome del cittadino
     * @return il <code>nome<code/> del cittadino
     */
    public String getNome() {
        return nome;
    }

    /**
     * Metodo setter per il nome del cittadino
     * @param nome il nuovo nome per il cittadino
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     *  Metodo getter per il cognome del cittadino
     * @return il <code>cognome</code> del cittadino
     */
    public String getCognome() {
        return cognome;
    }

    /**
     *  Metodo setter per il cognome del cittadino
     * @param cognome il nuovo cognome del cittadino
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     *  Metodo getter per il codice fiscale del cittadino
     * @return  il <code>codice fiscale</code> del cittadino
     */
    public String getCodicefiscale() {
        return codicefiscale;
    }

    /**
     *  Metodo setter per il codice fiscale del cittadino
     * @param codicefiscale il nuovo codice fiscale del cittadino
     */
    public void setCodicefiscale(String codicefiscale) {
        this.codicefiscale = codicefiscale;
    }

    /**
     *  Metodo getter per la mail del cittadino
     * @return la <code>mail</code> del cittadino
     */
    public String getMail() {
        return mail;
    }

    /**
     *  Metodo setter per la mail del cittadino
     * @param mail la nuova mail del cittadino
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     *  Metodo getter per l'userid del cittadino
     * @return l'<code>userid</code> del cittadino
     */
    public String getUserid() {
        return userid;
    }

    /**
     *  Metodo setter per l'userid del cittadino
     * @param userid il nuovo userid del cittadino
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }

    /**
     *  Metodo getter per la password del cittadino
     * @return la <code>password</code> del cittadino
     */
    public String getPassword() {
        return password;
    }

    /**
     *  Metodo setter per la password del cittadino
     * @param password la nuova password del cittadino
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *  Metodo getter per l'id del vaccino associato al cittadino
     * @return l'id del vaccino
     */
    public long getId_vaccino() {
        return id_vaccino;
    }

    /**
     *  Metodo setter per l'id del vaccino associato al cittadino
     * @param id_vaccino il nuovo id del vaccino
     */
    public void setId_vaccino(long id_vaccino) {
        this.id_vaccino = id_vaccino;
    }
}
