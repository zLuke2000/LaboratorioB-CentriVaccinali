package it.uninsubria.centrivaccinali.enumerator;

/**
 * Rappresenta i diversi tipi di centri vaccinali
 * @author ...
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
        switch (this) {
            case OSPEDALIERO: return "ospedaliero";
            case AZIENDALE: return "aziendale";
            case HUB: return "hub";
            default: throw new IllegalArgumentException();
        }
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
