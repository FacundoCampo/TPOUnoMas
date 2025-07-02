package model.estadosDelPartido;

import model.entity.Notificacion;
import model.entity.Partido;
import model.entity.PartidoContext;
import model.entity.Usuario;
import enums.EstadoPartido;

import java.util.Date;

public class PartidoArmadoI implements IEstadoPartido {


    @Override
    public void agregarJugador(PartidoContext contexto, Usuario jugador) {
        throw new IllegalStateException("Ya no se pueden agregar jugadores en estado 'Partido armado'.");
    }

    public void confirmar(PartidoContext contexto, Usuario jugador) {
        Partido partido = contexto.getPartido();

        if (partido == null || jugador == null) return;
        if (!partido.estaInscrito(jugador)) return;

        verificarTransicion(contexto);
    }

    @Override
    public void cancelar(PartidoContext contexto) {
        contexto.setEstadoActual(new Cancelado());
        notificar(contexto.getPartido(), "El partido ha sido cancelado.");
    }

    @Override
    public void finalizar(PartidoContext contexto) {
        verificarTransicion(contexto);
    }

    private void verificarTransicion(PartidoContext contexto) {
        Partido partido = contexto.getPartido();

        if (!partido.estaCompleto()) {
            contexto.setEstadoActual(new NecesitamosJugadores());
            notificar(partido, "Un jugador se retiró. Necesitamos más jugadores.");
            return;
        }

        long horas = partido.getHorasHastaPartido();

        if (horas <= 0) {
            contexto.setEstadoActual(new EnJuego());
            notificar(partido, "¡El partido ha comenzado!");
        } else if (horas <= 2) {
            contexto.setEstadoActual(new Confirmado());
            notificar(partido, "El partido ha sido confirmado. ¡Nos vemos en la cancha!");
        }
    }

    @Override
    public String getNombre() {
        return EstadoPartido.PARTIDOARMADOI.name();
    }

    private void notificar(Partido partido, String mensaje) {
        Notificacion n = new Notificacion();
        n.setIdPartido(partido.getId());
        n.setMensaje(mensaje);
        n.setFechaCreacion(new Date());
        partido.agregarNotificacion(n);
    }
}
