package model.estadosDelPartido;

import enums.EstadoPartido;
import model.entity.Notificacion;
import model.entity.Partido;
import model.entity.PartidoContext;
import model.entity.Usuario;

import java.util.Date;

public class EnJuego implements IEstadoPartido {

    @Override
    public void agregarJugador(PartidoContext contexto, Usuario jugador) {
        throw new IllegalStateException("No se pueden agregar jugadores mientras el partido está en juego.");
    }

    @Override
    public void cancelar(PartidoContext contexto) {
        throw new IllegalStateException("No se puede cancelar un partido que ya está en juego.");
    }

    @Override
    public void finalizar(PartidoContext contexto) {
        Partido partido = contexto.getPartido();

        if (!debeFinalizarse(partido)) {
            throw new IllegalStateException("El partido aún no terminó.");
        }

        // Agregar al historial de todos los jugadores
        for (Usuario jugador : partido.getJugadoresInscritos()) {
            jugador.agregarPartidoAHistorial(partido.getId());
        }

        contexto.setEstadoActual(new Finalizado());
        notificar(partido, "El partido ha finalizado.");
    }

    @Override
    public String getNombre() {
        return EstadoPartido.ENJUEGO.name();
    }

    private boolean debeFinalizarse(Partido partido) {
        if (partido.getFechaHora() == null) return false;

        long tiempoActual = System.currentTimeMillis();
        long tiempoInicio = partido.getFechaHora().getTime();
        long duracionMs = partido.getDuracion() * 60 * 1000L;

        return tiempoActual >= (tiempoInicio + duracionMs);
    }

    private void notificar(Partido partido, String mensaje) {
        Notificacion n = new Notificacion();
        n.setIdPartido(partido.getId());
        n.setMensaje(mensaje);
        n.setFechaCreacion(new Date());
        partido.agregarNotificacion(n);
    }
}
