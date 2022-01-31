package it.uninsubria.centrivaccinali.util;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Tooltip;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe utilizzata dall'applicazione per effettuare il controllo che il testo inserito in input rispetti i parametri
 * impostida noi
 */
public class ControlloParametri {


    /**
     * Lista contente tutte le province italiane.
     */
    private static final ArrayList<String> listaProvince = new ArrayList<>();


    /**
     * Rifermento al singleton <code>CssHelper</code> che permette la gestione degli stili per i vari componenti grafici.
     * @see CssHelper
     */
    private final CssHelper cssHelper = CssHelper.getInstance();


    /**
     * Singleton di <code>ControlloParametri</code> che permette di controllare che le credenziali inserite rispettino i requisiti richiesti.
     * @see ControlloParametri
     */
    private static ControlloParametri instance = null;


    /**
     * Riferimento al pattern usato.
     */
    private Pattern rPattern;


    /**
     * Riferimento al matcher usato.
     */
    private Matcher rMatcher;


    /**
     * Costruttore della classe principale.
     */
    private ControlloParametri() {}


    /**
     * Ritorna l'istanza di questa classe. In caso di valore <code>null</code>, crea il singleton.
     * @return Istanza della classe stessa
     */
    public static ControlloParametri getInstance(){
        if(instance == null){
            instance = new ControlloParametri();
            InputStream file = CentriVaccinali.class.getResourceAsStream("province.txt");
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(file)));
                String line;
                while ((line = br.readLine()) != null) {
                    listaProvince.add(line);
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }


    /**
     * Metodo per il controllo che il testo inserito in input corrisponda a soli numeri o lettere dell'alfabeto.
     * @param tic TextInputControl in cui si andr&amp;agrave a scrivere nell'interfaccia
     * @param minChar valore minimo del numero di caratteri che deve contenere intesto inserito
     * @param maxChar valore massimo del numero di caratteri che deve contenere intesto inserito
     * @return valore  booleano <code>true</code> nel caso rispetti i requisiti, altrimenti <code>false</code>
     */
    public boolean testoSempliceConNumeri(TextInputControl tic, int minChar, int maxChar) {
        rPattern = Pattern.compile("[\\w\\d\\s]{" + minChar + "," + maxChar + "}");
        rMatcher = rPattern.matcher(tic.getText().trim());
        if(rMatcher.matches()) {
            cssHelper.toValid(tic);
            return true;
        } else {
            cssHelper.toError(tic, new Tooltip("immettere da " + minChar + " a " + maxChar + " caratteri"));
            return false;
        }
    }


    /**
     * Metodo per il controllo della non presenza di numeri del testo inserito in input.
     * @param tic TextInputControl in cui si andr&amp;agrave a scrivere nell'interfaccia.
     * @param minChar valore minimo del numero di caratteri che deve contenere intesto inserito.
     * @param maxChar valore massimo del numero di caratteri che deve contenere intesto inserito.
     * @return valore  booleano <code>true</code> nel caso rispetti i requisiti, altrimenti <code>false</code>
     */
    public boolean testoSempliceSenzaNumeri(TextInputControl tic, int minChar, int maxChar) {
        rPattern = Pattern.compile("[\\D]{" + minChar + "," + maxChar + "}");
        rMatcher = rPattern.matcher(tic.getText().trim());
        if(rMatcher.matches()) {
            cssHelper.toValid(tic);
            return true;
        } else {
            cssHelper.toError(tic, new Tooltip("immettere da " + minChar + " a " + maxChar + " caratteri"));
            return false;
        }
    }


    /**
     * Metodo per il controllo dell'occorrenza di numeri presenti del testo inserito in input.
     * @param tic TextInputControl in cui si andr&amp;agrave a scrivere nell'interfaccia.
     * @param minChar valore minimo dell'occorenze di numeri presenti.
     * @param maxChar valore massimo dell'occorenze di numeri presenti.
     * @return valore booleano <code>true</code> nel caso rispetti i requisiti, altrimenti <code>false</code>
     */
    public boolean numeri(TextInputControl tic, int minChar, int maxChar) {
        if(minChar == maxChar) {
            rPattern = Pattern.compile("[\\d]{" + minChar + "}");
        } else {
            rPattern = Pattern.compile("[\\d]{" + minChar + "," + maxChar + "}");
        }
        rMatcher = rPattern.matcher(tic.getText().trim());
        if(rMatcher.matches()) {
            cssHelper.toValid(tic);
            return true;
        } else {
            if(minChar == maxChar) {
                cssHelper.toError(tic, new Tooltip("immettere " + minChar + " numeri"));
            } else {
                cssHelper.toError(tic, new Tooltip("immettere da " + minChar + " a " + maxChar +" numeri"));
            }
            return  false;
        }
    }


    /**
     * Metodo per il controllo del numero civico inserito in input
     * @param tic TextInputControl in cui si andr&amp;agrave a scrivere il numero civico nell'interfaccia
     * @return valore booleano <code>true</code> nel caso rispetti i requisiti, altrimenti <code>false</code>
     */
    public boolean numeroCivico(TextInputControl tic) {
        // Controllo numero civico (minimo UN numero ed eventualmente UNA lettera alla fine e massimo CINQUE caratteri)
        if(tic.getText().trim().length() <= 0) {
            cssHelper.toError(tic, new Tooltip("Inserire almeno UN numero"));
        } else if(tic.getText().trim().length() > 5) {
            cssHelper.toError(tic, new Tooltip("Massimo 5 numeri o \n Massimo 4 numeri e 1 lettera"));
        } else {
            // Converto il numero in intero per controllare se composto da solo numeri
            try {
                Integer.parseInt(tic.getText());
                cssHelper.toValid(tic);
                return true;
            } catch (NumberFormatException nfe1) {
                // Rimuovo Ultima lettera e controllo se composto da solo numeri
                try {
                    Integer.parseInt(tic.getText().replaceAll(".$", ""));
                    cssHelper.toValid(tic);
                    return true;
                } catch (NumberFormatException nfe2) {
                    cssHelper.toError(tic, new Tooltip("Consentita solo una lettera alla fine"));
                }
            }
        }
        return false;
    }


    /**
     * Metodo per il controllo dell provincia inserita  in input
     * @param tic TextInputControl in cui si andr&amp;agrave a scrivere la provincia nell'interfaccia
     * @return valore booleano <code>true</code> nel caso rispetti i requisiti, altrimenti <code>false</code>
     */
    public boolean provincia(TextInputControl tic) {
        rPattern = Pattern.compile("[a-zA-z]{2}");
        rMatcher = rPattern.matcher(tic.getText().trim());
        tic.setText(tic.getText().toUpperCase(Locale.ROOT));
        tic.positionCaret(tic.getText().length());
        if(rMatcher.matches()) {
            if (listaProvince.contains(tic.getText().trim())){
                cssHelper.toValid(tic);
                return true;
            }
            else {
                cssHelper.toError(tic, new Tooltip("provincia non esistente"));
            }
        } else {
            cssHelper.toError(tic, new Tooltip("solo 2 lettere maiuscole ammesse"));
        }
        return false;
    }


    /**
     * Metodo per il controllo della password inserita in input
     * @param tic TextInputControl in cui si andr&amp;agrave a scrivere la password nell'interfaccia
     * @return valore booleano <code>true</code> nel caso rispetti i requisiti, altrimenti <code>false</code>
     */
    public boolean password(TextInputControl tic) {
        String err ="";
        boolean res = true;
        if(tic.getText().trim().length() < 8) {
            res = false;
            //Stringhe errore password
            String err1 = "Password troppo corta (almeno 8 caratteri)";
            err = err + "\n" + err1;
        }
        rPattern = Pattern.compile(".*[a-z].*$");
        rMatcher = rPattern.matcher(tic.getText().trim());
        if(!rMatcher.matches()) {
            res = false;
            String err2 = "Deve contenere almeno una lettera minuscola";
            err = err + "\n" + err2;
        }
        rPattern = Pattern.compile(".*[A-Z].*$");
        rMatcher = rPattern.matcher(tic.getText().trim());
        if(!rMatcher.matches()) {
            res = false;
            String err3 = "Deve contenere almeno una lettera maiuscola";
            err = err + "\n" + err3;
        }
        rPattern = Pattern.compile(".*[0-8].*$");
        rMatcher = rPattern.matcher(tic.getText().trim());
        if(!rMatcher.matches()) {
            res = false;
            String err4 = "Deve contenere almeno un numero";
            err = err + "\n" + err4;
        }

        if(res) {
            cssHelper.toValid(tic);
        } else {
            cssHelper.toError(tic, new Tooltip(err));
        }
        return res;
    }


    /**
     * Metodo per il controllo del codice fiscale inserita in input
     * @param tic TextInputControl in cui si andr&amp;agrave a scrivere il codice fiscale nell'interfaccia
     * @return valore booleano <code>true</code> nel caso rispetti i requisiti, altrimenti <code>false</code>
     */
    public boolean codiceFiscale(TextInputControl tic) {
        tic.setText(tic.getText().toUpperCase());
        tic.positionCaret(tic.getText().length());
        if(tic.getText().trim().length() > 16) {
            cssHelper.toError(tic, new Tooltip("Codice fiscale non valido"));
            return false;
        }  else {
            rPattern = Pattern.compile("^(([A-Z]){6})(\\d{2})([A-Z])(\\d{2})([A-Z])(\\d{3})([A-Z])$");
            rMatcher = rPattern.matcher(tic.getText().trim());
            if(rMatcher.matches()) {
                cssHelper.toValid(tic);
                return true;
            } else {
                cssHelper.toError(tic, new Tooltip("Codice fiscale non valido"));
                return false;
            }
        }
    }


    /**
     * Metodo per il controllo dell'inserimento della data nel DataPicker
     * @param dp DataPicker in cui si andra ad inserire la data
     * @return valore booleano <code>true</code> nel caso rispetti i requisiti, altrimenti <code>false</code>
     */
    public boolean data(DatePicker dp) {
        if(dp.getEditor().getText().isBlank()) {
            cssHelper.toError(dp, new Tooltip("Selezionare la data"));
            return false;
        } else {
            cssHelper.toValid(dp);
            return true;
        }
    }


    /**
     * Metodo per il controllo dell'e-mail inserita in input
     * @param tic TextInputControl in cui si andr&amp;agrave a scrivere l'e-mail nell'interfaccia
     * @return valore booleano <code>true</code> nel caso rispetti i requisiti, altrimenti <code>false</code>
     */
    public boolean email(TextInputControl tic) {
        if(tic.getText().trim().length() <= 0) {
            cssHelper.toError(tic, new Tooltip("Inserire email"));
            return false;
        }  else {
            rPattern = Pattern.compile("^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$");
            rMatcher = rPattern.matcher(tic.getText().trim());
            if(rMatcher.matches()) {
                cssHelper.toValid(tic);
                return true;
            } else {
                cssHelper.toError(tic, new Tooltip("Email non valida"));
                return false;
            }
        }
    }


    /**
     * Metodo per il controllo dell'uguaglianza tra le due password
     * @param tic1 TextInputControl in cui si andr&amp;agrave a scrivere la prima password
     * @param tic2 TextInputControl in cui si andr&amp;agrave a scrivere la seconda password
     * @return valore booleano <code>true</code> nel caso rispetti i requisiti, altrimenti <code>false</code>
     */
    public boolean checkSamePassword(TextInputControl tic1, TextInputControl tic2) {
        if(tic1.getText().trim().equals(tic2.getText().trim())) {
            cssHelper.toValid(tic2);
            return true;
        } else {
            cssHelper.toError(tic2, new Tooltip("Le password non coincidono"));
            return false;
        }
    }


    /**
     * Metodo per il criptaggio della password
     * @param pwdPlain Stringa in cui compiere il criptaggio
     * @return la stringa criptata
     */
    public String encryptPassword(String pwdPlain) {
        StringBuilder sB = new StringBuilder();
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(pwdPlain.getBytes());
            byte[] bytes = m.digest();
            for (byte aByte : bytes) {
                sB.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sB.toString();
    }
}
