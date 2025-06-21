package model.estadosDelPartido;

import model.entity.Notificacion;
import model.entity.Partido;
import model.entity.Usuario;
import java.util.Date;

public class NecesitamosJugadores implements IEstadoPartido {

    @Override
    public boolean manejarNuevoJugador(Partido contexto, Usuario jugador) {
        System.out.println("[NecesitamosJugadores] Intentando agregar jugador: " + (jugador != null ? jugador.getEmail() : "null"));

        if (contexto == null) {
            System.out.println("[NecesitamosJugadores] El contexto del partido es null.");
            return false;
        }

        if (jugador == null) {
            System.out.println("[NecesitamosJugadores] El jugador es null.");
            return false;
        }

        if (contexto.estaCompleto()) {
            System.out.println("[NecesitamosJugadores] El partido ya está completo.");
            return false;
        }

        if (contexto.estaInscrito(jugador)) {
            System.out.println("[NecesitamosJugadores] El jugador ya está inscrito en el partido.");
            return false;
        }

        boolean agregado = contexto.agregarJugadorDirecto(jugador);
        System.out.println("[NecesitamosJugadores] Resultado de agregar jugador directamente: " + agregado);

        if (agregado) {
            System.out.println("[NecesitamosJugadores] Agregado correctamente. Agregando a historial y verificando transición.");
            jugador.agregarPartidoAHistorial(contexto);
            verificarTransicion(contexto);
        } else {
            System.out.println("[NecesitamosJugadores] No se pudo agregar el jugador.");
        }

        return agregado;
    }

    @Override
    public boolean manejarConfirmacion(Partido contexto, Usuario jugador) {
        System.out.println("[NecesitamosJugadores] No se manejan confirmaciones en este estado");
        return false;
    }

    @Override
    public void manejarCancelacion(Partido contexto) {
        System.out.println("[NecesitamosJugadores] Partido cancelado por falta de jugadores");
        contexto.setEstado(new Cancelado());
        notificarCancelacion(contexto);
    }

    @Override
    public void verificarTransicion(Partido contexto) {
        System.out.println("[NecesitamosJugadores] Verificando si partido está completo");
        if (contexto != null && contexto.estaCompleto()) {
            contexto.setEstado(new PartidoArmadoI());
            notificarPartidoArmado(contexto);
        }
    }

    @Override
    public String getNombre() { return "Necesitamos jugadores"; }

    @Override
    public boolean puedeAgregarJugadores() { return true; }

    @Override
    public boolean puedeCancelar() { return true; }

    private void notificarCancelacion(Partido contexto) {
        try {
            Notificacion notificacion = new Notificacion();
            notificacion.setIdPartido(contexto.getId());
            notificacion.setMensaje("El partido ha sido cancelado");
            notificacion.setFechaCreacion(new Date());
            contexto.agregarNotificacion(notificacion);
        } catch (Exception e) {
            System.err.println("Error al notificar cancelación: " + e.getMessage());
        }
    }

    private void notificarPartidoArmado(Partido contexto) {
        try {
            Notificacion notificacion = new Notificacion();
            notificacion.setIdPartido(contexto.getId());
            notificacion.setMensaje("¡El partido está armado! Se alcanzó el número de jugadores necesarios.");
            notificacion.setFechaCreacion(new Date());
            contexto.agregarNotificacion(notificacion);
        } catch (Exception e) {
            System.err.println("Error al notificar partido armado: " + e.getMessage());
        }
    }

}
