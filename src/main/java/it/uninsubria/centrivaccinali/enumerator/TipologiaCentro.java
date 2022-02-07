//Centore Luca 740951 VA
//Lattarulo Luca 742597 VA
//Marelli Samuele 742495 VA
//Pintonello Christian 741112 VA
package it.uninsubria.centrivaccinali.enumerator;

/**
 * Rappresenta i diversi tipi di centri vaccinali
 * @author Centore Luca 740951
 * @author Lattarulo Luca 742597
 * @author Marelli Samuele
 * @author Pintonello Christian 741112
 */
public enum TipologiaCentro {
    /**
     * Tipologia ospedaliero.
     */
    OSPEDALIERO,
    /**
     * Tipologia aziendale
     */
    AZIENDALE,
    /**
     * Tipologia hub
     */
    HUB;


    /**
     * Ritorna la stringa rappresentante la tipologia del centro vaccinale
     * @return la stringa che rappresenta la tipologia del centro
     */
    @Override
    public String toString() {
        return switch (this) {
            case OSPEDALIERO -> "ospedaliero";
            case AZIENDALE -> "aziendale";
            case HUB -> "hub";
        };
    }


    /**
     * Ritorna la tipologia di centro corrispondete alla stringa fornita
     * @param str la stringa da cui si vuole ottnere la corrispondente tipologia di centro vaccinale
     * @return la tipologia di centro vaccinale corrispondete alla stringa fornita come parametro
     * @throws IllegalArgumentException se la stringa fornita non corrisponde ad alcuna tipologia di centro
     */
    public static TipologiaCentro getValue(String str) {
        for (TipologiaCentro t : TipologiaCentro.values()) {
            if (t.toString().equalsIgnoreCase(str)) {
                return t;
            }
        }
        throw new IllegalArgumentException();
    }
}
