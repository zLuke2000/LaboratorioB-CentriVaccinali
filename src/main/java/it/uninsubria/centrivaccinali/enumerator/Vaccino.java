//Centore Luca 740951 VA
//Lattarulo Luca 742597 VA
//Marelli Samuele 742495 VA
//Pintonello Christian 741112 VA
package it.uninsubria.centrivaccinali.enumerator;


/**
 * Rappresenta le diverse tipologie di vaccino
 * @author Centore Luca 740951
 * @author Lattarulo Luca 742597
 * @author Marelli Samuele 742495
 * @author Pintonello Christian 741112
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
        return switch (this) {
            case ASTRAZENECA -> "astrazeneca";
            case JNJ -> "j&j";
            case MODERNA -> "moderna";
            case PFIZER -> "pfizer";
        };
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
