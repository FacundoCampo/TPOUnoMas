package state;

import model.Partido;
import model.Usuario;

/**
 * Estado inicial del partido cuando se necesitan más jugadores
 * Implementa el patrón State
 */
public class NecesitamosJugadores implements EstadoPartido {
    
    @Override
    public boolean manejarNuevoJugador(Partido contexto, Usuario jugador) {
        if (contexto == null || jugador == null) {
            return false;
        }
        
        // Verificar si el partido ya está completo
        if (contexto.estaCompleto()) {
            return false;
        }
        
        // Verificar si el jugador ya está inscrito
        if (contexto.estaInscrito(jugador)) {
            return false;
        }
        
        // Agregar el jugador al partido
        boolean agregado = contexto.agregarJugadorDirecto(jugador);
        
        if (agregado) {
            // Agregar el partido al historial del jugador
            jugador.agregarPartidoAHistorial(contexto);
            
            // Verificar si se debe transicionar de estado
            verificarTransicion(contexto);
        }
        
        return agregado;
    }
    
    @Override
    public boolean manejarConfirmacion(Partido contexto, Usuario jugador) {
        // En este estado no se manejan confirmaciones
        // Los jugadores se unen directamente
        return false;
    }
    
    @Override
    public void manejarCancelacion(Partido contexto) {
        if (contexto == null) {
            return;
        }
        
        // Cambiar el estado a cancelado
        contexto.setEstado(new Cancelado());
        
        // Notificar a todos los jugadores inscritos sobre la cancelación
        notificarCancelacion(contexto);
    }
    
    @Override
    public void verificarTransicion(Partido contexto) {
        if (contexto == null) {
            return;
        }
        
        // Si el partido está completo, transicionar a "Partido Armado"
        if (contexto.estaCompleto()) {
            contexto.setEstado(new PartidoArmado());
            
            // Notificar que el partido está armado
            notificarPartidoArmado(contexto);
        }
    }
    
    @Override
    public String getNombre() {
        return "Necesitamos jugadores";
    }
    
    @Override
    public boolean puedeAgregarJugadores() {
        return true;
    }
    
    @Override
    public boolean puedeCancelar() {
        return true;
    }
    
    @Override
    public boolean puedeConfirmar() {
        return false;
    }
    
    /**
     * Notifica a los jugadores que el partido fue cancelado
     * @param contexto el partido cancelado
     */
    private void notificarCancelacion(Partido contexto) {
        try {
            // Aquí se integraría con el servicio de notificaciones
            // Por ahora, solo agregamos una notificación al partido
            model.Notificacion notificacion = new model.Notificacion();
            notificacion.setIdPartido(contexto.getId());
            notificacion.setMensaje("El partido ha sido cancelado");
            notificacion.setFechaCreacion(new java.util.Date());
            
            contexto.agregarNotificacion(notificacion);
        } catch (Exception e) {
            System.err.println("Error al notificar cancelación: " + e.getMessage());
        }
    }
    
    /**
     * Notifica a los jugadores que el partido está armado
     * @param contexto el partido que está armado
     */
    private void notificarPartidoArmado(Partido contexto) {
        try {
            // Aquí se integraría con el servicio de notificaciones
            // Por ahora, solo agregamos una notificación al partido
            model.Notificacion notificacion = new model.Notificacion();
            notificacion.setIdPartido(contexto.getId());
            notificacion.setMensaje("¡El partido está armado! Se alcanzó el número de jugadores necesarios.");
            notificacion.setFechaCreacion(new java.util.Date());
            
            contexto.agregarNotificacion(notificacion);
        } catch (Exception e) {
            System.err.println("Error al notificar partido armado: " + e.getMessage());
        }
    }
    
    @Override
    public String toString() {
        return "NecesitamosJugadores{" +
                "nombre='" + getNombre() + '\'' +
                '}';
    }
}