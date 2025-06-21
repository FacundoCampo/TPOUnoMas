package model.estadosDelPartido;

import model.entity.Notificacion;
import model.entity.Partido;
import model.entity.Usuario;

public class PartidoArmadoI implements IEstadoPartido {

    @Override
    public boolean manejarNuevoJugador(Partido contexto, Usuario jugador) {
        System.out.println("[PartidoArmado] Ya no se pueden agregar jugadores");
        return false;
    }

    @Override
    public boolean manejarConfirmacion(Partido contexto, Usuario jugador) {
        System.out.println("[PartidoArmado] Confirmación recibida");
        if (contexto == null || jugador == null) return false;
        if (!contexto.estaInscrito(jugador)) return false;

        verificarTransicion(contexto);
        return true;
    }

    @Override
    public void manejarCancelacion(Partido contexto) {
        System.out.println("[PartidoArmado] Cancelando partido");
        contexto.setEstado(new Cancelado());
        notificarCancelacion(contexto);
    }

    @Override
    public void verificarTransicion(Partido contexto) {
        System.out.println("[PartidoArmado] Verificando transición de estado");
        if (contexto == null) return;

        long horasHastaPartido = contexto.getHorasHastaPartido();

        if (!contexto.estaCompleto()) {
            contexto.setEstado(new NecesitamosJugadores());
            notificarNecesitamosJugadores(contexto);
            return;
        }

        if (horasHastaPartido <= 0) {
            contexto.setEstado(new EnJuego());
            notificarEnJuego(contexto);
        } else if (horasHastaPartido <= 2) {
            contexto.setEstado(new Confirmado());
            notificarConfirmado(contexto);
        }

    }

    @Override
    public String getNombre() { return "Partido armado"; }
    @Override
    public boolean puedeCancelar() { return true; }
    @Override
    public boolean puedeConfirmar() { return true; }

    private void notificarCancelacion(Partido contexto) {
        Notificacion notificacion = new Notificacion();
        notificacion.setIdPartido(contexto.getId());
        notificacion.setMensaje("El partido ha sido cancelado");
        contexto.agregarNotificacion(notificacion);
    }

    private void notificarConfirmado(Partido contexto) {
        Notificacion notificacion = new Notificacion();
        notificacion.setIdPartido(contexto.getId());
        notificacion.setMensaje("El partido ha sido confirmado. ¡Nos vemos en la cancha!");
        contexto.agregarNotificacion(notificacion);
    }

    private void notificarEnJuego(Partido contexto) {
        Notificacion notificacion = new Notificacion();
        notificacion.setIdPartido(contexto.getId());
        notificacion.setMensaje("¡El partido ha comenzado!");
        contexto.agregarNotificacion(notificacion);
    }

    private void notificarNecesitamosJugadores(Partido contexto) {
        Notificacion notificacion = new Notificacion();
        notificacion.setIdPartido(contexto.getId());
        notificacion.setMensaje("Un jugador se retiró. Necesitamos más jugadores para completar el partido.");
        contexto.agregarNotificacion(notificacion);
    }
}
