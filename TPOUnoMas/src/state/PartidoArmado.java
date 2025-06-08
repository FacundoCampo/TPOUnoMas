package state;

import model.Partido;
import model.Usuario;
import model.Notificacion;
import java.util.Date;

public class PartidoArmado implements EstadoPartido {
    
    @Override
    public boolean manejarNuevoJugador(Partido contexto, Usuario jugador) {
        return false; // El partido ya está completo
    }
    
    @Override
    public boolean manejarConfirmacion(Partido contexto, Usuario jugador) {
        if (contexto == null || jugador == null) return false;
        if (!contexto.estaInscrito(jugador)) return false;
        
        verificarTransicion(contexto);
        return true;
    }
    
    @Override
    public void manejarCancelacion(Partido contexto) {
        if (contexto == null) return;
        contexto.setEstado(new Cancelado());
        notificarCancelacion(contexto);
    }
    
    @Override
    public void verificarTransicion(Partido contexto) {
        if (contexto == null) return;
        
        long horasHastaPartido = contexto.getHorasHastaPartido();
        
        if (horasHastaPartido <= 2 && horasHastaPartido > 0) {
            contexto.setEstado(new Confirmado());
            notificarConfirmado(contexto);
        } else if (horasHastaPartido <= 0) {
            contexto.setEstado(new EnJuego());
            notificarEnJuego(contexto);
        }
        
        if (!contexto.estaCompleto()) {
            contexto.setEstado(new NecesitamosJugadores());
            notificarNecesitamosJugadores(contexto);
        }
    }
    
    @Override
    public String getNombre() { return "Partido armado"; }
    
    @Override
    public boolean puedeCancelar() { return true; }
    
    @Override
    public boolean puedeConfirmar() { return true; }
    
    // Métodos de notificación similares a NecesitamosJugadores
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