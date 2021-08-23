package it.uninsubria.laboratoriob.models;

/**Classe per salvare le informazioni del cittadino registrato*/
public class Cittadino {
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

    public Cittadino(String nome, String cognome, String codicefiscale, String mail, String userid, String password, long id_vaccino) {
        this.nome = nome;
        this.cognome = cognome;
        this.codicefiscale = codicefiscale;
        this.mail = mail;
        this.userid = userid;
        this.password = password;
        this.id_vaccino = id_vaccino;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getCodicefiscale() {
        return codicefiscale;
    }

    public void setCodicefiscale(String codicefiscale) {
        this.codicefiscale = codicefiscale;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId_vaccino() {
        return id_vaccino;
    }

    public void setId_vaccino(long id_vaccino) {
        this.id_vaccino = id_vaccino;
    }
}
