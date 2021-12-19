package it.uninsubria.centrivaccinali.util;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Tooltip;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControlloParametri {

    /**
     *
     */
    private static final ArrayList<String> listaProvince = new ArrayList<>();
    /**
     *
     */
    private final CssHelper cssHelper = CssHelper.getInstance();
    /**
     *
     */
    private static ControlloParametri instance = null;
    /**
     *
     */
    private Pattern rPattern;
    /**
     *
     */
    private Matcher rMatcher;
    /**
     *
     */
    private ControlloParametri() {}

    /**
     *
     * @return
     */
    public static ControlloParametri getInstance(){
        if(instance == null){
            instance = new ControlloParametri();
            //salva le province sulla lista dal file .json
            try {
                JSONParser parser = new JSONParser();
                FileReader fr = new FileReader(Objects.requireNonNull(CentriVaccinali.class.getResource("province.json")).getPath());
                JSONArray a = (JSONArray)parser.parse(fr);
                for (Object o: a) {
                   listaProvince.add(o.toString());
                }
                fr.close();
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    /**
     *
     * @param tic
     * @param minChar
     * @param maxChar
     * @return
     */
    public boolean testoSempliceConNumeri(TextInputControl tic, int minChar, int maxChar) {
        rPattern = Pattern.compile("[A-Za-z\\d\\s]{" + minChar + "," + maxChar + "}");
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
     *
     * @param tic
     * @param minChar
     * @param maxChar
     * @return
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
     *
     * @param tic
     * @param minChar
     * @param maxChar
     * @return
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
     *
     * @param tic
     * @return
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
     *
     * @param tic
     * @return
     */
    public boolean provincia(TextInputControl tic) {
        rPattern = Pattern.compile("[A-Za-z]{2}");
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
     *
     * @param tic
     * @return
     */
    public boolean password(TextInputControl tic) {
        String err ="";
        boolean res = true;
        if(tic.getText().trim().length() < 8) {
            //cssHelper.toError(tic, new Tooltip("Password troppo corta"));
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
     *
     * @param tic
     * @return
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
     *
     * @param dp
     * @return
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
     *
     * @param tic
     * @return
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
     *
     * @param tic1
     * @param tic2
     * @return
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
     *
     * @param pwdPlain
     * @return
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
