package model.estadosDelPartido;

import enums.EstadoPartido;
import model.entity.Notificacion;
import model.entity.Partido;
import model.entity.PartidoContext;
import model.entity.Usuario;
import java.util.Date;

public class NecesitamosJugadores implements IEstadoPartido {

    @Override
    public void agregarJugador(PartidoContext contexto, Usuario jugador) {
        Partido partido = contexto.getPartido();

        if (partido == null || jugador == null) return;

        if (partido.estaCompleto() || partido.estaInscrito(jugador)) return;

        boolean agregado = partido.agregarJugadorDirecto(jugador);

        if (agregado) {
            jugador.agregarPartidoAHistorial(partido.getId());
            notificar(partido, "Jugador agregado: " + jugador.getEmail());

            if (partido.estaCompleto()) {
                notificar(partido, "¡El partido está armado!");
                contexto.setEstadoActual(new PartidoArmadoI());
            }
        }
    }

    @Override
    public void cancelar(PartidoContext contexto) {
        contexto.setEstadoActual(new Cancelado());
        notificar(contexto.getPartido(), "El partido ha sido cancelado.");
    }

    @Override
    public void finalizar(PartidoContext contexto) {
        throw new IllegalStateException("No se puede finalizar un partido donde se necesitan jugadores.");
    }

    @Override
    public String getNombre() {
        return EstadoPartido.NECESITAMOSJUGADORES.name();
    }

    @Override
    public boolean permiteCambioDeEstrategia() { return true; }

    private void notificar(Partido partido, String mensaje) {
        Notificacion n = new Notificacion();
        n.setIdPartido(partido.getId());
        n.setMensaje(mensaje);
        n.setFechaCreacion(new Date());
        partido.agregarNotificacion(n);
    }

}
