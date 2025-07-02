package model.entity;

import enums.EstadoPartido;
import enums.TipoEmparejamiento;

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
    private List<Notificacion> notificaciones;
    private TipoEmparejamiento tipoEmparejamiento;
    private String organizadorID;
    private EstadoPartido estado;

    public Partido(Deporte deporte, int duracion, String ubicacion, Date fechaHora, String organizadorID, TipoEmparejamiento tipoEmparejamiento) {
        this.deporte = deporte;
        this.duracion = duracion;
        this.ubicacion = ubicacion;
        this.fechaHora = fechaHora != null ? new Date(fechaHora.getTime()) : null;
        this.jugadoresInscritos = new ArrayList<>();
        this.notificaciones = new ArrayList<>();
        this.organizadorID = organizadorID;
        this.estado = EstadoPartido.NECESITAMOSJUGADORES;
        this.tipoEmparejamiento = tipoEmparejamiento;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Deporte getDeporte() { return deporte; }
    public void setDeporte(Deporte deporte) { this.deporte = deporte; }

    public int getDuracion() { return duracion; }
    public void setDuracion(int duracion) { this.duracion = duracion; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public Date getFechaHora() {
        return fechaHora != null ? new Date(fechaHora.getTime()) : null;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora != null ? new Date(fechaHora.getTime()) : null;
    }

    public List<Usuario> getJugadoresInscritos() {
        return new ArrayList<>(jugadoresInscritos);
    }

    public void setJugadoresInscritos(List<Usuario> jugadoresInscritos) {
        this.jugadoresInscritos = jugadoresInscritos != null ? new ArrayList<>(jugadoresInscritos) : new ArrayList<>();
    }

    public List<Notificacion> getNotificaciones() {
        return new ArrayList<>(notificaciones);
    }

    public void setNotificaciones(List<Notificacion> notificaciones) {
        this.notificaciones = notificaciones != null ? new ArrayList<>(notificaciones) : new ArrayList<>();
    }

    public TipoEmparejamiento getTipoEmparejamiento() {
        return tipoEmparejamiento;
    }

    public void setTipoEmparejamiento(TipoEmparejamiento tipoEmparejamiento) {
        this.tipoEmparejamiento = tipoEmparejamiento;
    }

    public String getOrganizadorID() {
        return organizadorID;
    }

    public void setOrganizadorID(String organizadorID) {
        this.organizadorID = organizadorID;
    }

    public EstadoPartido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPartido estado) {
        this.estado = estado;
    }

    /**
     * Agrega un jugador directamente (sin verificación de estado).
     */
    public boolean agregarJugadorDirecto(Usuario jugador) {
        if (jugador == null || jugadoresInscritos.contains(jugador)) return false;
        return jugadoresInscritos.add(jugador);
    }

    /**
     * Elimina un jugador directamente.
     */
    public boolean removerJugador(Usuario jugador) {
        return jugador != null && jugadoresInscritos.remove(jugador);
    }

    /**
     * Verifica si el partido ya tiene todos los jugadores requeridos.
     */
    public boolean estaCompleto() {
        if (deporte == null) return false;
        return jugadoresInscritos.size() >= deporte.getCantidadJugadoresEstandar();
    }

    /**
     * Verifica si un usuario ya está inscrito.
     */
    public boolean estaInscrito(Usuario usuario) {
        return usuario != null && jugadoresInscritos.contains(usuario);
    }

    /**
     * Agrega una notificación al partido.
     */
    public void agregarNotificacion(Notificacion notificacion) {
        if (notificacion != null) {
            if (notificacion.getIdPartido() == null && this.id != null) {
                notificacion.setIdPartido(this.id);
            }
            notificaciones.add(notificacion);
        }
    }

    /**
     * Retorna las horas (enteras) que faltan hasta el partido.
     */
    public long getHorasHastaPartido() {
        if (fechaHora == null) return Long.MAX_VALUE;
        long diferencia = fechaHora.getTime() - System.currentTimeMillis();
        return diferencia / (1000 * 60 * 60);
    }

    /**
     * Notifica a todos los jugadores inscritos.
     */
    public void notificarObservadores(String mensaje) {
        if (jugadoresInscritos != null) {
            for (Usuario jugador : jugadoresInscritos) {
                jugador.actualizar(mensaje);
            }
        }
    }
}
