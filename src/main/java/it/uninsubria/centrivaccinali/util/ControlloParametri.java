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
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControlloParametri {

    private final CssHelper cssHelper = CssHelper.getInstance();
    private static ControlloParametri instance = null;

    private Pattern rPattern;
    private Matcher rMatcher;
    private static final ArrayList<String> listaProvince = new ArrayList<>();

    //Stringhe errore password
    private final String err1 = "Password troppo corta (almeno 8 caratteri)";
    private final String err2 = "Deve contenere almeno una lettera minuscola";
    private final String err3 = "Deve contenere almeno una lettera maiuscola";
    private final String err4 = "Deve contenere almeno un numero";

    private ControlloParametri() {}

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

    public ArrayList<String> getProvince() {
        return  listaProvince;
    }

    public boolean testoSempliceConNumeri(TextInputControl tic, int minChar, int maxChar) {
        rPattern = Pattern.compile("[A-Za-z\\d]{" + minChar + "," + maxChar + "}");
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
            System.out.println("cp1");
            cssHelper.toError(tic, new Tooltip("immettere da " + minChar + " a " + maxChar + " caratteri"));
            System.out.println("cp2");
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

    public boolean password(TextInputControl tic) {
        String errTo0ltip = null;
        boolean res = true;
        if(tic.getText().trim().length() <= 8) {
            //cssHelper.toError(tic, new Tooltip("Password troppo corta"));
            res = false;
            if(errTo0ltip != null) {
                errTo0ltip = errTo0ltip + "\n" + err1;
            } else {
                errTo0ltip = err1;
            }
            //return false;
        }
        rPattern = Pattern.compile(".*[a-z].*$");
        rMatcher = rPattern.matcher(tic.getText().trim());
        if(!rMatcher.matches()) {
            System.out.println("NO min");
            res = false;
            if(errTo0ltip != null) {
                errTo0ltip = errTo0ltip + "\n" + err2;
            } else {
                errTo0ltip = err2;
            }
        }
        rPattern = Pattern.compile(".*[A-Z].*$");
        rMatcher = rPattern.matcher(tic.getText().trim());
        if(!rMatcher.matches()) {
            System.out.println("NO mai");
            res = false;
            if(errTo0ltip != null) {
                errTo0ltip = errTo0ltip + "\n" + err3;
            } else {
                errTo0ltip = err3;
            }
        }
        rPattern = Pattern.compile(".*[0-9].*$");
        rMatcher = rPattern.matcher(tic.getText().trim());
        if(!rMatcher.matches()) {
            System.out.println("NO num");
            res = false;
            if(errTo0ltip != null) {
                errTo0ltip = errTo0ltip + "\n" + err4;
            } else {
                errTo0ltip = err4;
            }
        }

        if(res) {
            cssHelper.toValid(tic);
        } else {
            cssHelper.toError(tic, new Tooltip(errTo0ltip));
        }
        return res;
        ///^((?=\S*?[A-Z])(?=\S*?[a-z])(?=\S*?[0-9]).{8,})\S$/
        /*
        rPattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$");
        rMatcher  = rPattern.matcher((tic.getText().trim()));
        if(rMatcher.matches()) {
            cssHelper.toValid(tic);
            return true;
        } else  {
            cssHelper.toError(tic, new Tooltip("Password non valida"));
            return false;
        }
         */
    }

    public boolean codiceFiscale(TextInputControl tic) {
        if(tic.getText().trim().length() > 16) {
            cssHelper.toError(tic, new Tooltip("Codice fiscale non valido"));
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

    public boolean data(DatePicker dp) {
        if(dp.getEditor().getText().isBlank()) {
            cssHelper.toError(dp, new Tooltip("Selezionare la data"));
            return false;
        } else {
            cssHelper.toValid(dp);
            return true;
        }
    }

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
