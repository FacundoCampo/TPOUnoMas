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
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public int getValor() {
        return valor;
    }
    
    public boolean esMayorOIgualQue(NivelJuego otroNivel) {
        return this.valor >= otroNivel.valor;
    }
    
    public boolean esMenorOIgualQue(NivelJuego otroNivel) {
        return this.valor <= otroNivel.valor;
    }
    
    public boolean estaEnRango(NivelJuego nivelMinimo, NivelJuego nivelMaximo) {
        return this.valor >= nivelMinimo.valor && this.valor <= nivelMaximo.valor;
    }
    
    public NivelJuego getSiguienteNivel() {
        switch (this) {
            case PRINCIPIANTE: return INTERMEDIO;
            case INTERMEDIO: return AVANZADO;
            case AVANZADO: return null;
            default: return null;
        }
    }
    
    public NivelJuego getNivelAnterior() {
        switch (this) {
            case PRINCIPIANTE: return null;
            case INTERMEDIO: return PRINCIPIANTE;
            case AVANZADO: return INTERMEDIO;
            default: return null;
        }
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
}