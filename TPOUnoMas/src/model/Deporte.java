package model;

import java.util.Objects;

/**
 * Clase que representa un deporte en el sistema
 * Contiene información básica sobre el deporte y sus características
 */
public class Deporte {
    
    private String id;
    private String nombre;
    private int cantidadJugadoresEstandar;
    
    /**
     * Constructor por defecto
     */
    public Deporte() {
    }
    
    /**
     * Constructor con parámetros
     * @param id identificador único del deporte
     * @param nombre nombre del deporte
     * @param cantidadJugadoresEstandar cantidad estándar de jugadores por equipo
     */
    public Deporte(String id, String nombre, int cantidadJugadoresEstandar) {
        this.id = id;
        this.nombre = nombre;
        this.cantidadJugadoresEstandar = cantidadJugadoresEstandar;
    }
    
    /**
     * Constructor sin ID (se puede asignar después)
     * @param nombre nombre del deporte
     * @param cantidadJugadoresEstandar cantidad estándar de jugadores por equipo
     */
    public Deporte(String nombre, int cantidadJugadoresEstandar) {
        this.nombre = nombre;
        this.cantidadJugadoresEstandar = cantidadJugadoresEstandar;
    }
    
    /**
     * Obtiene el ID del deporte
     * @return el ID del deporte
     */
    public String getId() {
        return id;
    }
    
    /**
     * Establece el ID del deporte
     * @param id el nuevo ID del deporte
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * Obtiene el nombre del deporte
     * @return el nombre del deporte
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Establece el nombre del deporte
     * @param nombre el nuevo nombre del deporte
     * @throws IllegalArgumentException si el nombre es null o vacío
     */
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del deporte no puede ser null o vacío");
        }
        this.nombre = nombre.trim();
    }
    
    /**
     * Obtiene la cantidad estándar de jugadores por equipo
     * @return la cantidad de jugadores estándar
     */
    public int getCantidadJugadoresEstandar() {
        return cantidadJugadoresEstandar;
    }
    
    /**
     * Establece la cantidad estándar de jugadores por equipo
     * @param cantidadJugadoresEstandar la nueva cantidad de jugadores estándar
     * @throws IllegalArgumentException si la cantidad es menor o igual a cero
     */
    public void setCantidadJugadoresEstandar(int cantidadJugadoresEstandar) {
        if (cantidadJugadoresEstandar <= 0) {
            throw new IllegalArgumentException("La cantidad de jugadores debe ser mayor a cero");
        }
        this.cantidadJugadoresEstandar = cantidadJugadoresEstandar;
    }
    
    /**
     * Verifica si este deporte es individual (1 jugador)
     * @return true si es individual, false en caso contrario
     */
    public boolean esDeporteIndividual() {
        return cantidadJugadoresEstandar == 1;
    }
    
    /**
     * Verifica si este deporte es de equipo (más de 1 jugador)
     * @return true si es de equipo, false en caso contrario
     */
    public boolean esDeporteDeEquipo() {
        return cantidadJugadoresEstandar > 1;
    }
    
    /**
     * Calcula el total de jugadores necesarios para un partido completo
     * (ambos equipos)
     * @return el total de jugadores necesarios
     */
    public int getTotalJugadoresNecesarios() {
        // Para deportes individuales como tenis, se necesitan 2 jugadores
        // Para deportes de equipo, se multiplica por 2 (dos equipos)
        return esDeporteIndividual() ? 2 : cantidadJugadoresEstandar * 2;
    }
    
    /**
     * Verifica si el deporte es válido (tiene todos los campos requeridos)
     * @return true si es válido, false en caso contrario
     */
    public boolean esValido() {
        return nombre != null && !nombre.trim().isEmpty() && cantidadJugadoresEstandar > 0;
    }
    
    /**
     * Crea deportes predefinidos comunes
     * @param tipoDeporte el tipo de deporte a crear
     * @return el deporte creado
     */
    public static Deporte crearDeportePredefinido(TipoDeporte tipoDeporte) {
        switch (tipoDeporte) {
            case FUTBOL:
                return new Deporte("FUTBOL", "Fútbol", 11);
            case BASQUET:
                return new Deporte("BASQUET", "Básquet", 5);
            case VOLEY:
                return new Deporte("VOLEY", "Vóley", 6);
            case TENIS:
                return new Deporte("TENIS", "Tenis", 1);
            case FUTBOL_5:
                return new Deporte("FUTBOL_5", "Fútbol 5", 5);
            case PADDLE:
                return new Deporte("PADDLE", "Pádel", 2);
            default:
                throw new IllegalArgumentException("Tipo de deporte no soportado: " + tipoDeporte);
        }
    }
    
    /**
     * Enumeración de tipos de deportes predefinidos
     */
    public enum TipoDeporte {
        FUTBOL, BASQUET, VOLEY, TENIS, FUTBOL_5, PADDLE
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Deporte deporte = (Deporte) obj;
        
        return Objects.equals(id, deporte.id) && 
               Objects.equals(nombre, deporte.nombre) &&
               cantidadJugadoresEstandar == deporte.cantidadJugadoresEstandar;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, cantidadJugadoresEstandar);
    }
    
    @Override
    public String toString() {
        return "Deporte{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", cantidadJugadoresEstandar=" + cantidadJugadoresEstandar +
                '}';
    }
    
    /**
     * Crea una representación simple del deporte para mostrar al usuario
     * @return string con formato "Nombre (X jugadores)"
     */
    public String toStringSimple() {
        return nombre + " (" + cantidadJugadoresEstandar + " jugadores)";
    }
    
    /**
     * Clona el deporte actual
     * @return una nueva instancia de Deporte con los mismos valores
     */
    public Deporte clone() {
        return new Deporte(this.id, this.nombre, this.cantidadJugadoresEstandar);
    }
}