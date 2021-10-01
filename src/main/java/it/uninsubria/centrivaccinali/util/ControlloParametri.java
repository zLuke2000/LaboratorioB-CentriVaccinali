package it.uninsubria.centrivaccinali.util;

import it.uninsubria.centrivaccinali.enumerator.Province;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Tooltip;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControlloParametri {

    private static ControlloParametri instance = null;

    private final CssHelper cssHelper = CssHelper.getInstance();
    private Pattern rPattern;
    private Matcher rMatcher;
    //private String infoErrorPassword = "Password non valida" + "\n" + "Deve contenre:" + "\n"  +
    //        "- Almeno 8 caratteri"  + "\n"
    //        + "- Almeno una lettere maiuscola" +
    //        "\n" + "- Almeno una lettere minuscola" + "\n" +
    //        "- Almeno un numero" + "\n" +"- Può contenere valori speciali";

    private ControlloParametri() {}

    public static ControlloParametri getInstance(){
        if(instance == null){
            instance = new ControlloParametri();
        }
        return instance;
    }

    public boolean testoSempliceConNumeri(TextInputControl tic, int minChar, int maxChar) {
        rPattern = Pattern.compile("[\\D\\d]{" + minChar + "," + maxChar + "}");
        rMatcher = rPattern.matcher(tic.getText().trim());
        if(rMatcher.matches()) {
            cssHelper.toValid(tic);
            return true;
        } else {
            cssHelper.toError(tic, new Tooltip("immettere da " + minChar + " a " + maxChar + " caratteri"));
            return false;
        }
    }

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

    public boolean numeroCivico(TextInputControl tic) {
        // Controllo numero civico (minimo UN numero ed eventualmente UNA lettera alla fine e massimo 5 caratteri)
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

    public boolean provincia(TextInputControl tic) {
        rPattern = Pattern.compile("[A-Za-z]{2}");
        rMatcher = rPattern.matcher(tic.getText().trim());
        tic.setText(tic.getText().toUpperCase(Locale.ROOT));
        tic.positionCaret(tic.getText().length());
        if(rMatcher.matches()) {
            try {
                Province.valueOf(tic.getText().trim());
                cssHelper.toValid(tic);
                return true;
            } catch (IllegalArgumentException iae){
                cssHelper.toError(tic, new Tooltip("sigla inesistente"));
                return false;
            }
        } else {
            cssHelper.toError(tic, new Tooltip("solo 2 lettere maiuscole ammesse"));
            return false;
        }
    }

    public boolean password(TextInputControl tic) {
        if(tic.getText().trim().length() <= 0) {
            return false;
        }
        rPattern  = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$");
        rMatcher  = rPattern.matcher((tic.getText().trim()));
        if(rMatcher.matches()) {
            cssHelper.toValid(tic);
            return true;
        } else  {
            cssHelper.toError(tic, new Tooltip("Password non valida"));
            return false;
        }
    }

    public boolean codiceFiscale(TextInputControl tic) {
        if(tic.getText().trim().length() > 16) {
            return false;
        }  else {
            rPattern = Pattern.compile("^(([a-z]|[A-Z]){6})(\\d{2})([a-z]|[A-Z])(\\d{2})([a-z]|[A-Z])(\\d{3})([a-z]|[A-Z])$");
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

    public boolean email(TextInputControl tic) {
        if(tic.getText().trim().length() > 16) {
            return false;
        }  else {
            rPattern = Pattern.compile("\\b[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,4}\\b");
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

    public boolean checkSamePassword(TextInputControl tic1, TextInputControl tic2) {
        if(tic1.getText().trim().equals(tic2.getText().trim())) {
            cssHelper.toValid(tic2);
            return true;
        } else {
            cssHelper.toError(tic2, new Tooltip("Le password non coincidono"));
            return false;
        }
    }
}
