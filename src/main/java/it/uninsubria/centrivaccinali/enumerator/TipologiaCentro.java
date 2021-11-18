package it.uninsubria.centrivaccinali.enumerator;

/**
 *
 */
public enum TipologiaCentro {
    OSPEDALIERO, AZIENDALE, HUB;

    @Override
    public String toString() {
        switch (this) {
            case OSPEDALIERO: return "ospedaliero";
            case AZIENDALE: return "aziendale";
            case HUB: return "hub";
            default: throw new IllegalArgumentException();
        }
    }

    public static TipologiaCentro getValue(String str) {
        for (TipologiaCentro t : TipologiaCentro.values()) {
            if (t.toString().equalsIgnoreCase(str)) {
                return t;
            }
        }
        throw new IllegalArgumentException();
    }
}
