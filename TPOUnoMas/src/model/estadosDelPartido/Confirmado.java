package model.estadosDelPartido;

import enums.EstadoPartido;
import model.entity.Notificacion;
import model.entity.Partido;
import model.entity.PartidoContext;
import model.entity.Usuario;

import java.util.Date;

public class Confirmado implements IEstadoPartido {

    @Override
    public void agregarJugador(PartidoContext contexto, Usuario jugador) {
        throw new IllegalStateException("No se pueden agregar jugadores en estado Confirmado.");
    }

    @Override
    public void cancelar(PartidoContext contexto) {
        contexto.setEstadoActual(new Cancelado("Cancelado después de haber sido confirmado."));

        Partido partido = contexto.getPartido();
        Notificacion n = new Notificacion();
        n.setIdPartido(partido.getId());
        n.setFechaCreacion(new Date());
        n.setMensaje("El partido confirmado fue cancelado por el organizador.");
        partido.agregarNotificacion(n);

        partido.notificarObservadores("El partido ha sido cancelado.");
    }

    @Override
    public void finalizar(PartidoContext contexto) {
        Partido partido = contexto.getPartido();

        if (!partido.estaCompleto()) {
            contexto.setEstadoActual(new NecesitamosJugadores());
            notificar(partido, "El partido ya no tiene suficientes jugadores.");
            return;
        }

        long horas = partido.getHorasHastaPartido();

        if (horas <= 0) {
            contexto.setEstadoActual(new EnJuego());
            notificar(partido, "¡El partido ha comenzado!");
        } else {
            throw new IllegalStateException("Aún no es la hora del partido.");
        }
    }

    @Override
    public String getNombre() {
        return EstadoPartido.CONFIRMADO.name();
    }

    private void notificar(Partido partido, String mensaje) {
        Notificacion n = new Notificacion();
        n.setIdPartido(partido.getId());
        n.setMensaje(mensaje);
        n.setFechaCreacion(new Date());
        partido.agregarNotificacion(n);
    }
}
