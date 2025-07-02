package model.estadosDelPartido;

import enums.EstadoPartido;
import model.entity.Notificacion;
import model.entity.Partido;
import model.entity.PartidoContext;
import model.entity.Usuario;
import java.util.Date;

public class Cancelado implements IEstadoPartido {

    private final Date fechaCancelacion;
    private final String motivoCancelacion;

    public Cancelado() {
        this.fechaCancelacion = new Date();
        this.motivoCancelacion = "No especificado";
    }

    public Cancelado(String motivo) {
        this.fechaCancelacion = new Date();
        this.motivoCancelacion = motivo != null ? motivo : "No especificado";
    }

    @Override
    public void agregarJugador(PartidoContext contexto, Usuario jugador) {
        throw new IllegalStateException("No se pueden agregar jugadores a un partido cancelado.");
    }

    @Override
    public void cancelar(PartidoContext contexto) {
        Partido partido = contexto.getPartido();
        Notificacion notificacion = new Notificacion();
        notificacion.setIdPartido(partido.getId());
        notificacion.setFechaCreacion(fechaCancelacion);
        notificacion.setMensaje("El partido ha sido cancelado. Motivo: " + motivoCancelacion);
        partido.agregarNotificacion(notificacion);

        partido.notificarObservadores("El partido ha sido cancelado.");
    }

    @Override
    public void finalizar(PartidoContext contexto) {
        throw new IllegalStateException("No se puede finalizar un partido cancelado.");
    }

    @Override
    public String getNombre() {
        return EstadoPartido.CANCELADO.name();
    }

    public Date getFechaCancelacion() {
        return fechaCancelacion;
    }

    public String getMotivoCancelacion() {
        return motivoCancelacion;
    }

}