package model;

import state.EstadoPartido;
import state.NecesitamosJugadores;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Clase que representa un partido en el sistema
 * Implementa el patrón State para manejar los diferentes estados del partido
 */
public class Partido {
    
    private String id;
    private Deporte deporte;
    private int duracion; // duración en minutos
    private String ubicacion;
    private Date fechaHora;
    private List<Usuario> jugadoresInscritos;
    private EstadoPartido estado;
    private List<Notificacion> notificaciones;
    
    /**
     * Constructor por defecto
     * Inicializa las listas y establece el estado inicial
     */
    public Partido() {
        this.jugadoresInscritos = new ArrayList<>();
        this.notificaciones = new ArrayList<>();
        this.estado = new NecesitamosJugadores();
    }
    
    /**
     * Constructor con parámetros básicos
     * @param deporte el deporte del partido
     * @param duracion duración del partido en minutos
     * @param ubicacion ubicación donde se realizará el partido
     * @param fechaHora fecha y hora del partido
     */
    public Partido(Deporte deporte, int duracion, String ubicacion, Date fechaHora) {
        this();
        this.deporte = deporte;
        this.duracion = duracion;
        this.ubicacion = ubicacion;
        this.fechaHora = fechaHora != null ? new Date(fechaHora.getTime()) : null;
    }
    
    /**
     * Constructor completo
     * @param id ID único del partido
     * @param deporte el deporte del partido
     * @param duracion duración del partido en minutos
     * @param ubicacion ubicación donde se realizará el partido
     * @param fechaHora fecha y hora del partido
     * @param estado estado inicial del partido
     */
    public Partido(String id, Deporte deporte, int duracion, String ubicacion, 
                   Date fechaHora, EstadoPartido estado) {
        this(deporte, duracion, ubicacion, fechaHora);
        this.id = id;
        if (estado != null) {
            this.estado = estado;
        }
    }
    
    /**
     * Obtiene el ID del partido
     * @return el ID del partido
     */
    public String getId() {
        return id;
    }
    
    /**
     * Establece el ID del partido
     * @param id el nuevo ID del partido
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * Obtiene el deporte del partido
     * @return el deporte del partido
     */
    public Deporte getDeporte() {
        return deporte;
    }
    
    /**
     * Establece el deporte del partido
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
     * Obtiene la duración del partido en minutos
     * @return la duración en minutos
     */
    public int getDuracion() {
        return duracion;
    }
    
    /**
     * Establece la duración del partido
     * @param duracion la nueva duración en minutos
     * @throws IllegalArgumentException si la duración es menor o igual a 0
     */
    public void setDuracion(int duracion) {
        if (duracion <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor a 0 minutos");
        }
        this.duracion = duracion;
    }
    
    /**
     * Obtiene la ubicación del partido
     * @return la ubicación del partido
     */
    public String getUbicacion() {
        return ubicacion;
    }
    
    /**
     * Establece la ubicación del partido
     * @param ubicacion la nueva ubicación
     * @throws IllegalArgumentException si la ubicación es null o vacía
     */
    public void setUbicacion(String ubicacion) {
        if (ubicacion == null || ubicacion.trim().isEmpty()) {
            throw new IllegalArgumentException("La ubicación no puede estar vacía");
        }
        this.ubicacion = ubicacion.trim();
    }
    
    /**
     * Obtiene la fecha y hora del partido
     * @return la fecha y hora del partido
     */
    public Date getFechaHora() {
        return fechaHora != null ? new Date(fechaHora.getTime()) : null;
    }
    
    /**
     * Establece la fecha y hora del partido
     * @param fechaHora la nueva fecha y hora
     * @throws IllegalArgumentException si la fecha es null o en el pasado
     */
    public void setFechaHora(Date fechaHora) {
        if (fechaHora == null) {
            throw new IllegalArgumentException("La fecha y hora no pueden ser null");
        }
        
        if (fechaHora.before(new Date())) {
            throw new IllegalArgumentException("La fecha del partido no puede ser en el pasado");
        }
        
        this.fechaHora = new Date(fechaHora.getTime());
    }
    
    /**
     * Obtiene la lista de jugadores inscritos
     * @return lista de jugadores inscritos (no modificable directamente)
     */
    public List<Usuario> getJugadoresInscritos() {
        return new ArrayList<>(jugadoresInscritos);
    }
    
    /**
     * Establece la lista de jugadores inscritos
     * @param jugadoresInscritos nueva lista de jugadores
     */
    public void setJugadoresInscritos(List<Usuario> jugadoresInscritos) {
        this.jugadoresInscritos = jugadoresInscritos != null ? 
                                  new ArrayList<>(jugadoresInscritos) : 
                                  new ArrayList<>();
    }
    
    /**
     * Obtiene el estado actual del partido
     * @return el estado actual del partido
     */
    public EstadoPartido getEstado() {
        return estado;
    }
    
    /**
     * Establece el estado del partido
     * @param estado el nuevo estado
     * @throws IllegalArgumentException si el estado es null
     */
    public void setEstado(EstadoPartido estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser null");
        }
        this.estado = estado;
    }
    
    /**
     * Obtiene la lista de notificaciones del partido
     * @return lista de notificaciones (no modificable directamente)
     */
    public List<Notificacion> getNotificaciones() {
        return new ArrayList<>(notificaciones);
    }
    
    /**
     * Establece la lista de notificaciones
     * @param notificaciones nueva lista de notificaciones
     */
    public void setNotificaciones(List<Notificacion> notificaciones) {
        this.notificaciones = notificaciones != null ? 
                              new ArrayList<>(notificaciones) : 
                              new ArrayList<>();
    }
    
    /**
     * Agrega un jugador al partido
     * Utiliza el patrón State para determinar si se puede agregar
     * @param jugador el jugador a agregar
     * @return true si se agregó exitosamente, false en caso contrario
     * @throws IllegalArgumentException si el jugador es null
     */
    public boolean agregarJugador(Usuario jugador) {
        if (jugador == null) {
            throw new IllegalArgumentException("El jugador no puede ser null");
        }
        
        // Verificar si ya está inscrito
        if (jugadoresInscritos.contains(jugador)) {
            return false;
        }
        
        // Usar el patrón State para manejar la lógica de agregar jugador
        return estado.manejarNuevoJugador(this, jugador);
    }
    
    /**
     * Agrega un jugador directamente a la lista (método interno)
     * Este método es usado por los estados para agregar el jugador
     * @param jugador el jugador a agregar
     * @return true si se agregó, false si ya estaba presente
     */
    public boolean agregarJugadorDirecto(Usuario jugador) {
        if (jugador == null || jugadoresInscritos.contains(jugador)) {
            return false;
        }
        
        return jugadoresInscritos.add(jugador);
    }
    
    /**
     * Remueve un jugador del partido
     * @param jugador el jugador a remover
     * @return true si se removió exitosamente, false en caso contrario
     */
    public boolean removerJugador(Usuario jugador) {
        if (jugador == null) {
            return false;
        }
        
        return jugadoresInscritos.remove(jugador);
    }
    
    /**
     * Verifica si el partido está completo (tiene todos los jugadores necesarios)
     * @return true si está completo, false en caso contrario
     */
    public boolean estaCompleto() {
        if (deporte == null) {
            return false;
        }
        
        int jugadoresNecesarios = deporte.getTotalJugadoresNecesarios();
        return jugadoresInscritos.size() >= jugadoresNecesarios;
    }
    
    /**
     * Obtiene la cantidad de jugadores que faltan para completar el partido
     * @return número de jugadores faltantes (0 si está completo)
     */
    public int getJugadoresFaltantes() {
        if (deporte == null) {
            return 0;
        }
        
        int jugadoresNecesarios = deporte.getTotalJugadoresNecesarios();
        int jugadoresActuales = jugadoresInscritos.size();
        
        return Math.max(0, jugadoresNecesarios - jugadoresActuales);
    }
    
    /**
     * Verifica si un usuario específico está inscrito en el partido
     * @param usuario el usuario a verificar
     * @return true si está inscrito, false en caso contrario
     */
    public boolean estaInscrito(Usuario usuario) {
        return usuario != null && jugadoresInscritos.contains(usuario);
    }
    
    /**
     * Agrega una notificación al partido
     * @param notificacion la notificación a agregar
     * @throws IllegalArgumentException si la notificación es null
     */
    public void agregarNotificacion(Notificacion notificacion) {
        if (notificacion == null) {
            throw new IllegalArgumentException("La notificación no puede ser null");
        }
        
        // Establecer el ID del partido en la notificación si no está establecido
        if (notificacion.getIdPartido() == null && this.id != null) {
            notificacion.setIdPartido(this.id);
        }
        
        notificaciones.add(notificacion);
    }
    
    /**
     * Remueve una notificación del partido
     * @param notificacion la notificación a remover
     * @return true si se removió exitosamente, false en caso contrario
     */
    public boolean removerNotificacion(Notificacion notificacion) {
        return notificacion != null && notificaciones.remove(notificacion);
    }
    
    /**
     * Obtiene las notificaciones no leídas del partido
     * @return lista de notificaciones no leídas
     */
    public List<Notificacion> getNotificacionesNoLeidas() {
        return notificaciones.stream()
                           .filter(n -> !n.esLeida())
                           .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Verifica si el partido es en el futuro
     * @return true si es en el futuro, false en caso contrario
     */
    public boolean esEnElFuturo() {
        return fechaHora != null && fechaHora.after(new Date());
    }
    
    /**
     * Verifica si el partido es hoy
     * @return true si es hoy, false en caso contrario
     */
    public boolean esHoy() {
        if (fechaHora == null) {
            return false;
        }
        
        Date hoy = new Date();
        return esMismaFecha(fechaHora, hoy);
    }
    
    /**
     * Verifica si dos fechas son del mismo día
     * @param fecha1 primera fecha
     * @param fecha2 segunda fecha
     * @return true si son del mismo día, false en caso contrario
     */
    private boolean esMismaFecha(Date fecha1, Date fecha2) {
        java.util.Calendar cal1 = java.util.Calendar.getInstance();
        java.util.Calendar cal2 = java.util.Calendar.getInstance();
        
        cal1.setTime(fecha1);
        cal2.setTime(fecha2);
        
        return cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
               cal1.get(java.util.Calendar.DAY_OF_YEAR) == cal2.get(java.util.Calendar.DAY_OF_YEAR);
    }
    
    /**
     * Obtiene el tiempo restante hasta el partido en horas
     * @return horas hasta el partido (negativo si ya pasó)
     */
    public long getHorasHastaPartido() {
        if (fechaHora == null) {
            return 0;
        }
        
        long tiempoActual = System.currentTimeMillis();
        long tiempoPartido = fechaHora.getTime();
        long diferencia = tiempoPartido - tiempoActual;
        
        return diferencia / (60 * 60 * 1000); // Convertir a horas
    }
    
    /**
     * Verifica si el partido es válido (tiene todos los campos requeridos)
     * @return true si es válido, false en caso contrario
     */
    public boolean esValido() {
        return id != null && !id.trim().isEmpty() &&
               deporte != null && deporte.esValido() &&
               ubicacion != null && !ubicacion.trim().isEmpty() &&
               fechaHora != null &&
               duracion > 0 &&
               estado != null;
    }
    
    /**
     * Obtiene información resumida del partido
     * @return string con información básica del partido
     */
    public String getInformacionResumida() {
        StringBuilder info = new StringBuilder();
        info.append(deporte != null ? deporte.getNombre() : "Sin deporte");
        info.append(" - ").append(ubicacion != null ? ubicacion : "Sin ubicación");
        info.append(" - ").append(jugadoresInscritos.size());
        
        if (deporte != null) {
            info.append("/").append(deporte.getTotalJugadoresNecesarios());
        }
        
        info.append(" jugadores");
        info.append(" - ").append(estado != null ? estado.getNombre() : "Sin estado");
        
        return info.toString();
    }
    
    /**
     * Obtiene información completa del partido
     * @return string con toda la información del partido
     */
    public String getInformacionCompleta() {
        StringBuilder info = new StringBuilder();
        info.append("Partido ID: ").append(id).append("\n");
        info.append("Deporte: ").append(deporte != null ? deporte.getNombre() : "Sin deporte").append("\n");
        info.append("Ubicación: ").append(ubicacion).append("\n");
        info.append("Fecha y Hora: ").append(fechaHora).append("\n");
        info.append("Duración: ").append(duracion).append(" minutos\n");
        info.append("Estado: ").append(estado != null ? estado.getNombre() : "Sin estado").append("\n");
        info.append("Jugadores inscritos: ").append(jugadoresInscritos.size());
        
        if (deporte != null) {
            info.append("/").append(deporte.getTotalJugadoresNecesarios());
        }
        
        info.append("\nNotificaciones: ").append(notificaciones.size());
        
        return info.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Partido partido = (Partido) obj;
        
        return Objects.equals(id, partido.id) &&
               Objects.equals(deporte, partido.deporte) &&
               Objects.equals(ubicacion, partido.ubicacion) &&
               Objects.equals(fechaHora, partido.fechaHora);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, deporte, ubicacion, fechaHora);
    }
    
    @Override
    public String toString() {
        return "Partido{" +
                "id='" + id + '\'' +
                ", deporte=" + (deporte != null ? deporte.getNombre() : "null") +
                ", duracion=" + duracion +
                ", ubicacion='" + ubicacion + '\'' +
                ", fechaHora=" + fechaHora +
                ", jugadoresInscritos=" + jugadoresInscritos.size() +
                ", estado=" + (estado != null ? estado.getNombre() : "null") +
                ", notificaciones=" + notificaciones.size() +
                '}';
    }
    
    /**
     * Representación simple para mostrar al usuario
     * @return string con formato simple
     */
    public String toStringSimple() {
        StringBuilder simple = new StringBuilder();
        
        if (deporte != null) {
            simple.append(deporte.getNombre());
        }
        
        simple.append(" en ").append(ubicacion);
        
        if (fechaHora != null) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
            simple.append(" - ").append(sdf.format(fechaHora));
        }
        
        simple.append(" (").append(jugadoresInscritos.size());
        
        if (deporte != null) {
            simple.append("/").append(deporte.getTotalJugadoresNecesarios());
        }
        
        simple.append(")");
        
        return simple.toString();
    }
    
    /**
     * Clona el partido actual
     * @return una nueva instancia de Partido con los mismos valores
     */
    public Partido clone() {
        Partido clon = new Partido(id, deporte, duracion, ubicacion, fechaHora, estado);
        clon.setJugadoresInscritos(this.jugadoresInscritos);
        clon.setNotificaciones(this.notificaciones);
        return clon;
    }
}