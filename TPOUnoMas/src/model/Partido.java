package model;

import model.estadosDelPartido.IEstadoPartido;
import model.estadosDelPartido.NecesitamosJugadores;
import strategy.IEstrategiaEmparejamiento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Partido {
    private String id;
    private Deporte deporte;
    private int duracion;
    private String ubicacion;
    private Date fechaHora;
    private List<Usuario> jugadoresInscritos;
    private IEstadoPartido estado;
    private List<Notificacion> notificaciones;
    private IEstrategiaEmparejamiento estrategia;

    public Partido(Deporte deporte, int duracion, String ubicacion, Date fechaHora) {
        this.deporte = deporte;
        this.duracion = duracion;
        this.ubicacion = ubicacion;
        this.fechaHora = fechaHora != null ? new Date(fechaHora.getTime()) : null;
        this.estado = new NecesitamosJugadores();
    }
    
    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public Deporte getDeporte() { return deporte; }
    public void setDeporte(Deporte deporte) { this.deporte = deporte; }
    
    public int getDuracion() { return duracion; }
    public void setDuracion(int duracion) { this.duracion = duracion; }
    
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    
    public Date getFechaHora() { return fechaHora; }
    public void setFechaHora(Date fechaHora) { this.fechaHora = fechaHora; }
    
    public List<Usuario> getJugadoresInscritos() { return new ArrayList<>(jugadoresInscritos); }
    public void setJugadoresInscritos(List<Usuario> jugadoresInscritos) {
        this.jugadoresInscritos = jugadoresInscritos != null ? new ArrayList<>(jugadoresInscritos) : new ArrayList<>();
    }
    
    public IEstadoPartido getEstado() { return estado; }
    public void setEstado(IEstadoPartido estado) { this.estado = estado; }
    
    public List<Notificacion> getNotificaciones() { return new ArrayList<>(notificaciones); }
    public void setNotificaciones(List<Notificacion> notificaciones) {
        this.notificaciones = notificaciones != null ? new ArrayList<>(notificaciones) : new ArrayList<>();
    }
    
    // Métodos de lógica de negocio
    public boolean agregarJugadorDirecto(Usuario jugador) {
        if (jugador == null || jugadoresInscritos.contains(jugador)) return false;
        return jugadoresInscritos.add(jugador);
    }
    
    public boolean removerJugador(Usuario jugador) {
        return jugador != null && jugadoresInscritos.remove(jugador);
    }
    
    public boolean estaCompleto() {
        if (deporte == null) return false;
        int jugadoresNecesarios = deporte.getCantidadJugadoresEstandar();
        return jugadoresInscritos.size() >= jugadoresNecesarios;
    }
    
    public boolean estaInscrito(Usuario usuario) {
        return usuario != null && jugadoresInscritos.contains(usuario);
    }
    
    public void agregarNotificacion(Notificacion notificacion) {
        if (notificacion != null) {
            if (notificacion.getIdPartido() == null && this.id != null) {
                notificacion.setIdPartido(this.id);
            }
            notificaciones.add(notificacion);
        }
    }
    
    public long getHorasHastaPartido() {
        if (fechaHora == null) return 0;
        long tiempoActual = System.currentTimeMillis();
        long tiempoPartido = fechaHora.getTime();
        long diferencia = tiempoPartido - tiempoActual;
        return diferencia / (60 * 60 * 1000); // Convertir a horas
    }
    
    @Override
    public String toString() {
        return "Partido{id='" + id + "', deporte=" + 
               (deporte != null ? deporte.getNombre() : "null") + 
               ", ubicacion='" + ubicacion + "', estado=" + 
               (estado != null ? estado.getNombre() : "null") + "}";
    }
}