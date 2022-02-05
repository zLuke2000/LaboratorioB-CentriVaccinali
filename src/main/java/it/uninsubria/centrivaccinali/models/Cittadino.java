package it.uninsubria.centrivaccinali.models;

import java.io.Serializable;

/**
 * Classe per salvare le informazioni del cittadino registrato.
 *
 * @author Centore Luca 740951
 * @author Lattarulo Luca 742597
 * @author Marelli Samuele
 * @author Pintonello Christian 741112
 */
public class Cittadino implements Serializable {


    /**
     *
     */
    private static final long serialVersionUID = 1L;


    /**
     * Rappresenta il nome del cittadino
     */
    private String nome;


    /**
     * Rappresenta il cognome del cittadino
     */
    private String cognome;


    /**
     * Rappresenta il codice fiscale del cittadino
     */
    private String codice_fiscale;


    /**
     * Rappresenta la mail del cittadino
     */
    private String email;


    /**
     * Rappresenta l'userid con cui si registra il cittadino
     */
    private String userid;


    /**
     * Rappresenta la password dell'account per il cittadino
     */
    private String password;


    /**
     * Rappresenta l'id ricevuto al momento della vaccinazione
     */
    private long id_vaccinazione;


    /**
     * Costruttore vuoto dell'oggetto <code>Cittadino</code>
     */
    public Cittadino() {}


    /**
     *Costruttore primario per Cittadino
     * @param nome nome del cittadino
     * @param cognome cognome del cittadino
     * @param codicefiscale codice fiscale del cittadino
     * @param email mail del cittadino
     * @param userid userid di registrazione del cittadino
     * @param password password del cittadino
     * @param id_vaccino id di vaccinazione del cittadino
     */
    public Cittadino(String nome, String cognome, String codicefiscale, String email, String userid, String password, long id_vaccino) {
        this.nome = nome;
        this.cognome = cognome;
        this.codice_fiscale = codicefiscale;
        this.email = email;
        this.userid = userid;
        this.password = password;
        this.id_vaccinazione = id_vaccino;
    }


    /**
     * Metodo getter per il nome del cittadino
     * @return il <code>nome</code> del cittadino
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
     * Metodo getter per il cognome del cittadino
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
     * Metodo getter per il codice fiscale del cittadino
     * @return  il <code>codice fiscale</code> del cittadino
     */
    public String getCodice_fiscale() {
        return codice_fiscale;
    }


    /**
     * Metodo setter per il codice fiscale del cittadino
     * @param codice_fiscale il nuovo codice fiscale del cittadino
     */
    public void setCodice_fiscale(String codice_fiscale) {
        this.codice_fiscale = codice_fiscale;
    }


    /**
     * Metodo getter per la mail del cittadino
     * @return la <code>mail</code> del cittadino
     */
    public String getEmail() {
        return email;
    }


    /**
     * Metodo setter per la mail del cittadino
     * @param email la nuova mail del cittadino
     */
    public void setEmail(String email) {
        this.email = email;
    }


    /**
     * Metodo getter per l'userid del cittadino
     * @return l'<code>userid</code> del cittadino
     */
    public String getUserid() {
        return userid;
    }


    /**
     * Metodo setter per l'userid del cittadino
     * @param userid il nuovo userid del cittadino
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }


    /**
     * Metodo getter per la password del cittadino
     * @return la <code>password</code> del cittadino
     */
    public String getPassword() {
        return password;
    }


    /**
     * Metodo setter per la password del cittadino
     * @param password la nuova password del cittadino
     */
    public void setPassword(String password) {
        this.password = password;
    }


    /**
     * Metodo getter per l'id del vaccino associato al cittadino
     * @return l'id del vaccino
     */
    public long getId_vaccinazione() {
        return id_vaccinazione;
    }


    /**
     * Metodo setter per l'id del vaccino associato al cittadino
     * @param id_vaccinazione il nuovo id del vaccino
     */
    public void setId_vaccinazione(long id_vaccinazione) {
        this.id_vaccinazione = id_vaccinazione;
    }


    /**
     * Ritorna la stringa corrispondete al cittadino
     * @return la stringa che rappresenta il cittadino
     */
    @Override
    public String toString() {
        return "Cittadino{" +
                "nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", codice_fiscale='" + codice_fiscale + '\'' +
                ", email='" + email + '\'' +
                ", userid='" + userid + '\'' +
                ", password='" + password + '\'' +
                ", id_vaccinazione=" + id_vaccinazione +
                '}';
    }
}
