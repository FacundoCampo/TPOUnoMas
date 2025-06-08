package model;

import java.util.Objects;

/**
 * Clase que representa la relación entre un usuario y un deporte
 * Incluye información específica como nivel de juego y si es deporte favorito
 */
public class DeporteUsuario {
    
    private Deporte deporte;
    private NivelJuego nivelJuego;
    private boolean favorito;
    
    /**
     * Constructor por defecto
     */
    public DeporteUsuario() {
        this.favorito = false;
        this.nivelJuego = NivelJuego.PRINCIPIANTE; // Nivel por defecto
    }
    
    /**
     * Constructor con parámetros
     * @param deporte el deporte asociado
     * @param nivelJuego el nivel de juego del usuario en este deporte
     * @param favorito si es el deporte favorito del usuario
     */
    public DeporteUsuario(Deporte deporte, NivelJuego nivelJuego, boolean favorito) {
        this.deporte = deporte;
        this.nivelJuego = nivelJuego;
        this.favorito = favorito;
    }
    
    /**
     * Constructor simplificado (favorito = false por defecto)
     * @param deporte el deporte asociado
     * @param nivelJuego el nivel de juego del usuario en este deporte
     */
    public DeporteUsuario(Deporte deporte, NivelJuego nivelJuego) {
        this(deporte, nivelJuego, false);
    }
    
    /**
     * Obtiene el deporte asociado
     * @return el deporte
     */
    public Deporte getDeporte() {
        return deporte;
    }
    
    /**
     * Establece el deporte asociado
     * @param deporte el nuevo deporte
     * @throws IllegalArgumentException si el deporte es null
     */
    public void setDeporte(Deporte deporte) {
        if (deporte == null) {
            throw new IllegalArgumentException("El deporte no puede ser null");
        }
        this.deporte = deporte;
    }
    
    /**
     * Obtiene el nivel de juego del usuario en este deporte
     * @return el nivel de juego
     */
    public NivelJuego getNivelJuego() {
        return nivelJuego;
    }
    
    /**
     * Establece el nivel de juego del usuario en este deporte
     * @param nivelJuego el nuevo nivel de juego
     * @throws IllegalArgumentException si el nivel es null
     */
    public void setNivelJuego(NivelJuego nivelJuego) {
        if (nivelJuego == null) {
            throw new IllegalArgumentException("El nivel de juego no puede ser null");
        }
        this.nivelJuego = nivelJuego;
    }
    
    /**
     * Verifica si este es el deporte favorito del usuario
     * @return true si es favorito, false en caso contrario
     */
    public boolean isFavorito() {
        return favorito;
    }
    
    /**
     * Establece si este es el deporte favorito del usuario
     * @param favorito true si es favorito, false en caso contrario
     */
    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }
    
    /**
     * Verifica si el usuario es principiante en este deporte
     * @return true si es principiante, false en caso contrario
     */
    public boolean esPrincipiante() {
        return nivelJuego == NivelJuego.PRINCIPIANTE;
    }
    
    /**
     * Verifica si el usuario es intermedio en este deporte
     * @return true si es intermedio, false en caso contrario
     */
    public boolean esIntermedio() {
        return nivelJuego == NivelJuego.INTERMEDIO;
    }
    
    /**
     * Verifica si el usuario es avanzado en este deporte
     * @return true si es avanzado, false en caso contrario
     */
    public boolean esAvanzado() {
        return nivelJuego == NivelJuego.AVANZADO;
    }
    
    /**
     * Mejora el nivel de juego del usuario (si es posible)
     * @return true si se pudo mejorar el nivel, false si ya era el máximo
     */
    public boolean mejorarNivel() {
        NivelJuego siguienteNivel = nivelJuego.getSiguienteNivel();
        if (siguienteNivel != null) {
            this.nivelJuego = siguienteNivel;
            return true;
        }
        return false;
    }
    
    /**
     * Reduce el nivel de juego del usuario (si es posible)
     * @return true si se pudo reducir el nivel, false si ya era el mínimo
     */
    public boolean reducirNivel() {
        NivelJuego nivelAnterior = nivelJuego.getNivelAnterior();
        if (nivelAnterior != null) {
            this.nivelJuego = nivelAnterior;
            return true;
        }
        return false;
    }
    
    /**
     * Verifica si el nivel de juego está dentro del rango especificado
     * @param nivelMinimo nivel mínimo requerido
     * @param nivelMaximo nivel máximo permitido
     * @return true si está dentro del rango, false en caso contrario
     */
    public boolean cumpleRangoNivel(NivelJuego nivelMinimo, NivelJuego nivelMaximo) {
        if (nivelMinimo == null || nivelMaximo == null) {
            return true; // Si no hay restricciones, cumple
        }
        return nivelJuego.estaEnRango(nivelMinimo, nivelMaximo);
    }
    
    /**
     * Verifica si el DeporteUsuario es válido (tiene todos los campos requeridos)
     * @return true si es válido, false en caso contrario
     */
    public boolean esValido() {
        return deporte != null && deporte.esValido() && nivelJuego != null;
    }
    
    /**
     * Obtiene el nombre del deporte
     * @return el nombre del deporte o "Deporte no definido" si es null
     */
    public String getNombreDeporte() {
        return deporte != null ? deporte.getNombre() : "Deporte no definido";
    }
    
    /**
     * Obtiene la descripción del nivel de juego
     * @return la descripción del nivel de juego
     */
    public String getDescripcionNivel() {
        return nivelJuego != null ? nivelJuego.getDescripcion() : "Nivel no definido";
    }
    
    /**
     * Crea una representación completa del DeporteUsuario
     * @return string con información completa
     */
    public String getInformacionCompleta() {
        StringBuilder info = new StringBuilder();
        info.append(getNombreDeporte());
        info.append(" - Nivel: ").append(getDescripcionNivel());
        if (favorito) {
            info.append(" ⭐ (Favorito)");
        }
        return info.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        DeporteUsuario that = (DeporteUsuario) obj;
        
        return favorito == that.favorito &&
               Objects.equals(deporte, that.deporte) &&
               nivelJuego == that.nivelJuego;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(deporte, nivelJuego, favorito);
    }
    
    @Override
    public String toString() {
        return "DeporteUsuario{" +
                "deporte=" + (deporte != null ? deporte.getNombre() : "null") +
                ", nivelJuego=" + nivelJuego +
                ", favorito=" + favorito +
                '}';
    }
    
    /**
     * Crea una representación simple para mostrar al usuario
     * @return string con formato simple
     */
    public String toStringSimple() {
        return getNombreDeporte() + " (" + getDescripcionNivel() + ")" + 
               (favorito ? " ⭐" : "");
    }
    
    /**
     * Clona el DeporteUsuario actual
     * @return una nueva instancia de DeporteUsuario con los mismos valores
     */
    public DeporteUsuario clone() {
        Deporte deporteClonado = deporte != null ? deporte.clone() : null;
        return new DeporteUsuario(deporteClonado, nivelJuego, favorito);
    }
    
    /**
     * Compara el nivel de juego con otro DeporteUsuario del mismo deporte
     * @param otroDeporteUsuario el otro DeporteUsuario a comparar
     * @return entero negativo si este nivel es menor, 0 si son iguales, positivo si es mayor
     * @throws IllegalArgumentException si los deportes no coinciden
     */
    public int compararNivel(DeporteUsuario otroDeporteUsuario) {
        if (otroDeporteUsuario == null) {
            throw new IllegalArgumentException("El DeporteUsuario a comparar no puede ser null");
        }
        
        if (!Objects.equals(this.deporte, otroDeporteUsuario.deporte)) {
            throw new IllegalArgumentException("Solo se pueden comparar niveles del mismo deporte");
        }
        
        return Integer.compare(this.nivelJuego.getValor(), otroDeporteUsuario.nivelJuego.getValor());
    }
}