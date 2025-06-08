package model;

/**
 * Enumeración que representa los diferentes niveles de juego de un usuario
 * Utilizada para categorizar la habilidad de los jugadores en diferentes deportes
 */
public enum NivelJuego {
    
    /**
     * Nivel para jugadores que están comenzando o tienen poca experiencia
     */
    PRINCIPIANTE("Principiante", 1),
    
    /**
     * Nivel para jugadores con experiencia moderada
     */
    INTERMEDIO("Intermedio", 2),
    
    /**
     * Nivel para jugadores con mucha experiencia y habilidad
     */
    AVANZADO("Avanzado", 3);
    
    private final String descripcion;
    private final int valor;
    
    /**
     * Constructor del enum NivelJuego
     * @param descripcion descripción textual del nivel
     * @param valor valor numérico para comparaciones
     */
    NivelJuego(String descripcion, int valor) {
        this.descripcion = descripcion;
        this.valor = valor;
    }
    
    /**
     * Obtiene la descripción del nivel de juego
     * @return descripción del nivel
     */
    public String getDescripcion() {
        return descripcion;
    }
    
    /**
     * Obtiene el valor numérico del nivel para comparaciones
     * @return valor numérico del nivel
     */
    public int getValor() {
        return valor;
    }
    
    /**
     * Verifica si este nivel es mayor o igual al nivel especificado
     * @param otroNivel el nivel a comparar
     * @return true si este nivel es mayor o igual, false en caso contrario
     */
    public boolean esMayorOIgualQue(NivelJuego otroNivel) {
        return this.valor >= otroNivel.valor;
    }
    
    /**
     * Verifica si este nivel es menor o igual al nivel especificado
     * @param otroNivel el nivel a comparar
     * @return true si este nivel es menor o igual, false en caso contrario
     */
    public boolean esMenorOIgualQue(NivelJuego otroNivel) {
        return this.valor <= otroNivel.valor;
    }
    
    /**
     * Verifica si este nivel está dentro del rango especificado (inclusive)
     * @param nivelMinimo el nivel mínimo del rango
     * @param nivelMaximo el nivel máximo del rango
     * @return true si está dentro del rango, false en caso contrario
     */
    public boolean estaEnRango(NivelJuego nivelMinimo, NivelJuego nivelMaximo) {
        return this.valor >= nivelMinimo.valor && this.valor <= nivelMaximo.valor;
    }
    
    /**
     * Obtiene el nivel siguiente (si existe)
     * @return el nivel siguiente o null si es el más alto
     */
    public NivelJuego getSiguienteNivel() {
        switch (this) {
            case PRINCIPIANTE:
                return INTERMEDIO;
            case INTERMEDIO:
                return AVANZADO;
            case AVANZADO:
                return null; // Ya es el nivel más alto
            default:
                return null;
        }
    }
    
    /**
     * Obtiene el nivel anterior (si existe)
     * @return el nivel anterior o null si es el más bajo
     */
    public NivelJuego getNivelAnterior() {
        switch (this) {
            case PRINCIPIANTE:
                return null; // Ya es el nivel más bajo
            case INTERMEDIO:
                return PRINCIPIANTE;
            case AVANZADO:
                return INTERMEDIO;
            default:
                return null;
        }
    }
    
    /**
     * Convierte un string a NivelJuego
     * @param nivel el string a convertir
     * @return el NivelJuego correspondiente
     * @throws IllegalArgumentException si el string no corresponde a ningún nivel
     */
    public static NivelJuego fromString(String nivel) {
        if (nivel == null || nivel.trim().isEmpty()) {
            throw new IllegalArgumentException("El nivel no puede ser null o vacío");
        }
        
        String nivelUpperCase = nivel.trim().toUpperCase();
        
        for (NivelJuego nivelJuego : NivelJuego.values()) {
            if (nivelJuego.name().equals(nivelUpperCase) || 
                nivelJuego.descripcion.toUpperCase().equals(nivelUpperCase)) {
                return nivelJuego;
            }
        }
        
        throw new IllegalArgumentException("Nivel de juego no válido: " + nivel);
    }
    
    /**
     * Obtiene todos los niveles disponibles como array de strings
     * @return array con las descripciones de todos los niveles
     */
    public static String[] getAllDescripciones() {
        NivelJuego[] niveles = values();
        String[] descripciones = new String[niveles.length];
        
        for (int i = 0; i < niveles.length; i++) {
            descripciones[i] = niveles[i].getDescripcion();
        }
        
        return descripciones;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
}