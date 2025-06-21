package enums;

public enum NivelJuego {
    PRINCIPIANTE("Principiante", 1),
    INTERMEDIO("Intermedio", 2),
    AVANZADO("Avanzado", 3);

    private final String descripcion;
    private final int valor;

    NivelJuego(String descripcion, int valor) {
        this.descripcion = descripcion;
        this.valor = valor;
    }

    public boolean estaEnRango(NivelJuego nivelMinimo, NivelJuego nivelMaximo) {
        return this.valor >= nivelMinimo.valor && this.valor <= nivelMaximo.valor;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}