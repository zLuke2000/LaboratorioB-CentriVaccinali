package it.uninsubria.centrivaccinali.enumerator;


/**
 * Rappresenta le diverse tipologie di vaccino
 * @author ...
 */
public enum Vaccino {
    /**
     * Vaccino pfizer.
     */
    PFIZER,
    /**
     * Vaccino astrazeneca.
     */
    ASTRAZENECA,
    /**
     * Vaccino moderna.
     */
    MODERNA,
    /**
     * Vaccino j&amp;j.
     */
    JNJ;


    /**
     * Ritorna la stringa rappresentante la tipologia di vaccino
     * @return la stringa che rappresenta la tipologia di vaccino
     */
    @Override
    public String toString() {
        switch (this) {
            case ASTRAZENECA: return "astrazeneca";
            case JNJ: return "j&j";
            case MODERNA: return "moderna";
            case PFIZER: return "pfizer";
            default: throw new IllegalArgumentException();
        }
    }


    /**
     * Ritorna la tipologia di vaccino corrispondete alla stringa fornita
     * @param str la stringa da cui si vuole ottnere la corrispondente tipologia di vaccino
     * @return la tipologia di vaccino corrispondete alla stringa fornita come parametro
     * @throws IllegalArgumentException se la stringa fornita non corrisponde ad alcuna tipologia di vaccino
     */
    public static Vaccino getValue(String str) {
        for (Vaccino v : values()) {
            if (v.toString().equalsIgnoreCase(str)) {
                return v;
            }
        }
        throw new IllegalArgumentException();
    }
}
