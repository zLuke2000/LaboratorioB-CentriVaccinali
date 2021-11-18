package it.uninsubria.centrivaccinali.enumerator;

public enum Vaccino {
    PFIZER, ASTRAZENECA, MODERNA, JNJ;

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

    public static Vaccino getValue(String str) {
        for (Vaccino v : Vaccino.values()) {
            if (v.toString().equalsIgnoreCase(str)) {
                return v;
            }
        }
        throw new IllegalArgumentException();
    }
}
