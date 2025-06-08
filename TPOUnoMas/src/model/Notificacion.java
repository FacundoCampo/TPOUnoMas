package model;

import java.util.Date;
import java.util.Objects;

public class Notificacion {
    private String id;
    private String idPartido;
    private String idUsuario;
    private String mensaje;
    private Date fechaCreacion;
    private boolean leida;
    
    public Notificacion() {
        this.fechaCreacion = new Date();
        this.leida = false;
    }
    
    public Notificacion(String mensaje, String idUsuario) {
        this();
        this.mensaje = mensaje;
        this.idUsuario = idUsuario;
    }
    
    public Notificacion(String idPartido, String idUsuario, String mensaje) {
        this(mensaje, idUsuario);
        this.idPartido = idPartido;
    }
    
    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getIdPartido() { return idPartido; }
    public void setIdPartido(String idPartido) { this.idPartido = idPartido; }
    
    public String getIdUsuario() { return idUsuario; }
    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }
    
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    
    public Date getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public boolean esLeida() { return leida; }
    public void setLeida(boolean leida) { this.leida = leida; }
    
    public void marcarComoLeida() { this.leida = true; }
    
    public boolean esNotificacionDePartido() {
        return idPartido != null && !idPartido.trim().isEmpty();
    }
    
    @Override
    public String toString() {
        return "Notificacion{id='" + id + "', mensaje='" + mensaje + "', leida=" + leida + "}";
    }
}