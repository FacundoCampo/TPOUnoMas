package state;

import model.Partido;
import model.Usuario;

/**
 * Estado del partido cuando se ha alcanzado el número de jugadores necesarios
 * pero aún no todos han confirmado su asistencia
 * Implementa el patrón State
 */
public class PartidoArmado implements EstadoPartido {
    
    @Override
    public boolean manejarNuevoJugador(Partido contexto, Usuario jugador) {
        // En este estado no se pueden agregar más jugadores
        // El partido ya está completo
        return false;
    }
    
    @Override
    public boolean manejarConfirmacion(Partido contexto, Usuario jugador) {
        if (contexto == null || jugador == null) {
            return false;
        }
        
        // Verificar que el jugador esté inscrito en el partido
        if (!contexto.estaInscrito(jugador)) {
            return false;
        }
        
        // En este estado, los jugadores pueden confirmar su asistencia
        // La lógica de confirmación se manejaría en el servicio de partido
        // pero por ahora solo retornamos true si el jugador está inscrito
        
        // Verificar si todos los jugadores han confirmado
        verificarTransicion(contexto);
        
        return true;
    }
    
    @Override
    public void manejarCancelacion(Partido contexto) {
        if (contexto == null) {
            return;
        }
        
        // Cambiar el estado a cancelado
        contexto.setEstado(new Cancelado());
        
        // Notificar a todos los jugadores sobre la cancelación
        notificarCancelacion(contexto);
    }
    
    @Override
    public void verificarTransicion(Partido contexto) {
        if (contexto == null) {
            return;
        }
        
        // Si todos los jugadores confirmaron, pasar a estado "Confirmado"
        // Para simplificar, asumimos que después de un tiempo se confirma automáticamente
        // En una implementación real, se verificaría si todos confirmaron
        
        // Si falta poco tiempo para el partido y hay confirmaciones suficientes
        long horasHastaPartido = contexto.getHorasHastaPartido();
        
        if (horasHastaPartido <= 2 && horasHastaPartido > 0) {
            // Transicionar a confirmado si quedan menos de 2 horas
            contexto.setEstado(new Confirmado());
            notificarConfirmado(contexto);
        } else if (horasHastaPartido <= 0) {
            // Si ya es la hora del partido, pasar directamente a "En Juego"
            contexto.setEstado(new EnJuego());
            notificarEnJuego(contexto);
        }
        
        // Si algunos jugadores se retiran y el partido queda incompleto,
        // volver al estado "Necesitamos jugadores"
        if (!contexto.estaCompleto()) {
            contexto.setEstado(new NecesitamosJugadores());
            notificarNecesitamosJugadores(contexto);
        }
    }
    
    @Override
    public String getNombre() {
        return "Partido armado";
    }
    
    @Override
    public boolean puedeAgregarJugadores() {
        return false; // Ya está completo
    }
    
    @Override
    public boolean puedeCancelar() {
        return true;
    }
    
    @Override
    public boolean puedeConfirmar() {
        return true;
    }
    
    /**
     * Permite que un jugador se retire del partido
     * @param contexto el partido del que se retira
     * @param jugador el jugador que se retira
     * @return true si se retiró exitosamente
     */
    public boolean manejarRetiro(Partido contexto, Usuario jugador) {
        if (contexto == null || jugador == null) {
            return false;
        }
        
        boolean retirado = contexto.removerJugador(jugador);
        
        if (retirado) {
            // Verificar transición después del retiro
            verificarTransicion(contexto);
            notificarRetiro(contexto, jugador);
        }
        
        return retirado;
    }
    
    /**
     * Notifica que el partido fue cancelado
     * @param contexto el partido cancelado
     */
    private void notificarCancelacion(Partido contexto) {
        try {
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
     * Notifica que el partido fue confirmado
     * @param contexto el partido confirmado
     */
    private void notificarConfirmado(Partido contexto) {
        try {
            model.Notificacion notificacion = new model.Notificacion();
            notificacion.setIdPartido(contexto.getId());
            notificacion.setMensaje("El partido ha sido confirmado. ¡Nos vemos en la cancha!");
            notificacion.setFechaCreacion(new java.util.Date());
            
            contexto.agregarNotificacion(notificacion);
        } catch (Exception e) {
            System.err.println("Error al notificar confirmación: " + e.getMessage());
        }
    }
    
    /**
     * Notifica que el partido comenzó
     * @param contexto el partido que comenzó
     */
    private void notificarEnJuego(Partido contexto) {
        try {
            model.Notificacion notificacion = new model.Notificacion();
            notificacion.setIdPartido(contexto.getId());
            notificacion.setMensaje("¡El partido ha comenzado!");
            notificacion.setFechaCreacion(new java.util.Date());
            
            contexto.agregarNotificacion(notificacion);
        } catch (Exception e) {
            System.err.println("Error al notificar inicio: " + e.getMessage());
        }
    }
    
    /**
     * Notifica que se necesitan más jugadores
     * @param contexto el partido que necesita jugadores
     */
    private void notificarNecesitamosJugadores(Partido contexto) {
        try {
            model.Notificacion notificacion = new model.Notificacion();
            notificacion.setIdPartido(contexto.getId());
            notificacion.setMensaje("Un jugador se retiró. Necesitamos más jugadores para completar el partido.");
            notificacion.setFechaCreacion(new java.util.Date());
            
            contexto.agregarNotificacion(notificacion);
        } catch (Exception e) {
            System.err.println("Error al notificar necesidad de jugadores: " + e.getMessage());
        }
    }
    
    /**
     * Notifica que un jugador se retiró
     * @param contexto el partido
     * @param jugador el jugador que se retiró
     */
    private void notificarRetiro(Partido contexto, Usuario jugador) {
        try {
            model.Notificacion notificacion = new model.Notificacion();
            notificacion.setIdPartido(contexto.getId());
            notificacion.setMensaje("El jugador " + jugador.getNombre() + " se retiró del partido.");
            notificacion.setFechaCreacion(new java.util.Date());
            
            contexto.agregarNotificacion(notificacion);
        } catch (Exception e) {
            System.err.println("Error al notificar retiro: " + e.getMessage());
        }
    }
    
    @Override
    public String toString() {
        return "PartidoArmado{" +
                "nombre='" + getNombre() + '\'' +
                '}';
    }
}