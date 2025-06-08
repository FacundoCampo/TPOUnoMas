package model;

import java.util.Date;
import java.util.Objects;

/**
 * Clase que representa una notificación del sistema
 * Contiene información sobre mensajes enviados a los usuarios
 */
public class Notificacion {
    
    private String id;
    private String idPartido;
    private String idUsuario;
    private String mensaje;
    private Date fechaCreacion;
    private boolean leida;
    
    /**
     * Constructor por defecto
     * Inicializa la fecha de creación como la fecha actual y leída como false
     */
    public Notificacion() {
        this.fechaCreacion = new Date();
        this.leida = false;
    }
    
    /**
     * Constructor con parámetros básicos
     * @param mensaje el contenido del mensaje
     * @param idUsuario ID del usuario destinatario
     */
    public Notificacion(String mensaje, String idUsuario) {
        this();
        this.mensaje = mensaje;
        this.idUsuario = idUsuario;
    }
    
    /**
     * Constructor con parámetros para notificación de partido
     * @param idPartido ID del partido relacionado
     * @param idUsuario ID del usuario destinatario
     * @param mensaje el contenido del mensaje
     */
    public Notificacion(String idPartido, String idUsuario, String mensaje) {
        this(mensaje, idUsuario);
        this.idPartido = idPartido;
    }
    
    /**
     * Constructor completo
     * @param id ID único de la notificación
     * @param idPartido ID del partido relacionado
     * @param idUsuario ID del usuario destinatario
     * @param mensaje el contenido del mensaje
     * @param fechaCreacion fecha de creación de la notificación
     * @param leida estado de lectura de la notificación
     */
    public Notificacion(String id, String idPartido, String idUsuario, String mensaje, 
                       Date fechaCreacion, boolean leida) {
        this.id = id;
        this.idPartido = idPartido;
        this.idUsuario = idUsuario;
        this.mensaje = mensaje;
        this.fechaCreacion = fechaCreacion != null ? new Date(fechaCreacion.getTime()) : new Date();
        this.leida = leida;
    }
    
    /**
     * Obtiene el ID de la notificación
     * @return el ID de la notificación
     */
    public String getId() {
        return id;
    }
    
    /**
     * Establece el ID de la notificación
     * @param id el nuevo ID
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * Obtiene el ID del partido relacionado con la notificación
     * @return el ID del partido o null si no está relacionada con un partido
     */
    public String getIdPartido() {
        return idPartido;
    }
    
    /**
     * Establece el ID del partido relacionado
     * @param idPartido el ID del partido
     */
    public void setIdPartido(String idPartido) {
        this.idPartido = idPartido;
    }
    
    /**
     * Obtiene el ID del usuario destinatario
     * @return el ID del usuario destinatario
     */
    public String getIdUsuario() {
        return idUsuario;
    }
    
    /**
     * Establece el ID del usuario destinatario
     * @param idUsuario el ID del usuario
     * @throws IllegalArgumentException si el ID del usuario es null o vacío
     */
    public void setIdUsuario(String idUsuario) {
        if (idUsuario == null || idUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del usuario no puede estar vacío");
        }
        this.idUsuario = idUsuario;
    }
    
    /**
     * Obtiene el mensaje de la notificación
     * @return el contenido del mensaje
     */
    public String getMensaje() {
        return mensaje;
    }
    
    /**
     * Establece el mensaje de la notificación
     * @param mensaje el nuevo mensaje
     * @throws IllegalArgumentException si el mensaje es null o vacío
     */
    public void setMensaje(String mensaje) {
        if (mensaje == null || mensaje.trim().isEmpty()) {
            throw new IllegalArgumentException("El mensaje no puede estar vacío");
        }
        this.mensaje = mensaje.trim();
    }
    
    /**
     * Obtiene la fecha de creación de la notificación
     * @return la fecha de creación
     */
    public Date getFechaCreacion() {
        return fechaCreacion != null ? new Date(fechaCreacion.getTime()) : null;
    }
    
    /**
     * Establece la fecha de creación de la notificación
     * @param fechaCreacion la nueva fecha de creación
     */
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion != null ? new Date(fechaCreacion.getTime()) : new Date();
    }
    
    /**
     * Verifica si la notificación ha sido leída
     * @return true si ha sido leída, false en caso contrario
     */
    public boolean esLeida() {
        return leida;
    }
    
    /**
     * Verifica si la notificación ha sido leída (método alternativo)
     * @return true si ha sido leída, false en caso contrario
     */
    public boolean isLeida() {
        return leida;
    }
    
    /**
     * Establece el estado de lectura de la notificación
     * @param leida nuevo estado de lectura
     */
    public void setLeida(boolean leida) {
        this.leida = leida;
    }
    
    /**
     * Marca la notificación como leída
     * También actualiza la fecha de lectura (implícitamente)
     */
    public void marcarComoLeida() {
        this.leida = true;
    }
    
    /**
     * Marca la notificación como no leída
     */
    public void marcarComoNoLeida() {
        this.leida = false;
    }
    
    /**
     * Verifica si la notificación está relacionada con un partido
     * @return true si tiene un ID de partido asociado, false en caso contrario
     */
    public boolean esNotificacionDePartido() {
        return idPartido != null && !idPartido.trim().isEmpty();
    }
    
    /**
     * Verifica si la notificación es reciente (creada en las últimas 24 horas)
     * @return true si es reciente, false en caso contrario
     */
    public boolean esReciente() {
        if (fechaCreacion == null) {
            return false;
        }
        
        long tiempoActual = System.currentTimeMillis();
        long tiempoCreacion = fechaCreacion.getTime();
        long diferencia = tiempoActual - tiempoCreacion;
        
        // 24 horas en milisegundos
        long unDiaEnMs = 24 * 60 * 60 * 1000;
        
        return diferencia <= unDiaEnMs;
    }
    
    /**
     * Obtiene la antigüedad de la notificación en horas
     * @return número de horas desde la creación
     */
    public long getAntiguedadEnHoras() {
        if (fechaCreacion == null) {
            return 0;
        }
        
        long tiempoActual = System.currentTimeMillis();
        long tiempoCreacion = fechaCreacion.getTime();
        long diferencia = tiempoActual - tiempoCreacion;
        
        return diferencia / (60 * 60 * 1000); // Convertir a horas
    }
    
    /**
     * Obtiene la antigüedad de la notificación en días
     * @return número de días desde la creación
     */
    public long getAntiguedadEnDias() {
        return getAntiguedadEnHoras() / 24;
    }
    
    /**
     * Verifica si la notificación es válida (tiene todos los campos requeridos)
     * @return true si es válida, false en caso contrario
     */
    public boolean esValida() {
        return id != null && !id.trim().isEmpty() &&
               idUsuario != null && !idUsuario.trim().isEmpty() &&
               mensaje != null && !mensaje.trim().isEmpty() &&
               fechaCreacion != null;
    }
    
    /**
     * Obtiene el tipo de notificación basado en el contenido del mensaje
     * @return tipo de notificación inferido del mensaje
     */
    public TipoNotificacion getTipoNotificacion() {
        if (mensaje == null) {
            return TipoNotificacion.GENERAL;
        }
        
        String mensajeLower = mensaje.toLowerCase();
        
        if (mensajeLower.contains("nuevo partido")) {
            return TipoNotificacion.NUEVO_PARTIDO;
        } else if (mensajeLower.contains("confirmado")) {
            return TipoNotificacion.PARTIDO_CONFIRMADO;
        } else if (mensajeLower.contains("cancelado")) {
            return TipoNotificacion.PARTIDO_CANCELADO;
        } else if (mensajeLower.contains("cambio") || mensajeLower.contains("estado")) {
            return TipoNotificacion.CAMBIO_ESTADO;
        } else if (mensajeLower.contains("recordatorio")) {
            return TipoNotificacion.RECORDATORIO;
        }
        
        return TipoNotificacion.GENERAL;
    }
    
    /**
     * Crea una versión resumida del mensaje (máximo 50 caracteres)
     * @return mensaje resumido
     */
    public String getMensajeResumido() {
        if (mensaje == null) {
            return "";
        }
        
        if (mensaje.length() <= 50) {
            return mensaje;
        }
        
        return mensaje.substring(0, 47) + "...";
    }
    
    /**
     * Obtiene información completa de la notificación
     * @return string con toda la información de la notificación
     */
    public String getInformacionCompleta() {
        StringBuilder info = new StringBuilder();
        info.append("Notificación ID: ").append(id).append("\n");
        info.append("Usuario: ").append(idUsuario).append("\n");
        if (esNotificacionDePartido()) {
            info.append("Partido: ").append(idPartido).append("\n");
        }
        info.append("Mensaje: ").append(mensaje).append("\n");
        info.append("Fecha: ").append(fechaCreacion).append("\n");
        info.append("Estado: ").append(leida ? "Leída" : "No leída").append("\n");
        info.append("Tipo: ").append(getTipoNotificacion());
        
        return info.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Notificacion that = (Notificacion) obj;
        
        return Objects.equals(id, that.id) &&
               Objects.equals(idUsuario, that.idUsuario) &&
               Objects.equals(idPartido, that.idPartido);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, idUsuario, idPartido);
    }
    
    @Override
    public String toString() {
        return "Notificacion{" +
                "id='" + id + '\'' +
                ", idPartido='" + idPartido + '\'' +
                ", idUsuario='" + idUsuario + '\'' +
                ", mensaje='" + getMensajeResumido() + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", leida=" + leida +
                '}';
    }
    
    /**
     * Representación simple para mostrar al usuario
     * @return string con formato simple
     */
    public String toStringSimple() {
        String estado = leida ? "✓" : "●";
        return estado + " " + getMensajeResumido() + 
               " (" + getAntiguedadEnHoras() + "h)";
    }
    
    /**
     * Clona la notificación actual
     * @return una nueva instancia de Notificacion con los mismos valores
     */
    public Notificacion clone() {
        return new Notificacion(id, idPartido, idUsuario, mensaje, fechaCreacion, leida);
    }
    
    /**
     * Enum para los tipos de notificación
     */
    public enum TipoNotificacion {
        NUEVO_PARTIDO("Nuevo Partido"),
        PARTIDO_CONFIRMADO("Partido Confirmado"),
        PARTIDO_CANCELADO("Partido Cancelado"),
        CAMBIO_ESTADO("Cambio de Estado"),
        RECORDATORIO("Recordatorio"),
        GENERAL("General");
        
        private final String descripcion;
        
        TipoNotificacion(String descripcion) {
            this.descripcion = descripcion;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
        
        @Override
        public String toString() {
            return descripcion;
        }
    }
}