package enums;

public enum TipoEmparejamiento {
    CERCANIA("Cercanía"),
    HISTORIAL("Historial"),
    NIVEL("Nivel");

    private final String label;

    TipoEmparejamiento(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
