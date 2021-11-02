package it.uninsubria.centrivaccinali.enumerator;

public enum EventiAvversi {
    EA1, EA2, EA3, EA4, EA5, EA6;

    @Override
    public String toString() {
        switch (this) {
            case EA1: return "Mal di testa";
            case EA2: return "Febbre";
            case EA3: return "Dolori muscolari e articolari";
            case EA4: return "Linfoadenopatia";
            case EA5: return "Tachicardia";
            case EA6: return "Crisi ipertensiva";
            default: throw new IllegalArgumentException();
        }
    }
}
