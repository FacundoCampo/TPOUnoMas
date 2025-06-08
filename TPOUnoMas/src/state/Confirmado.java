package state;

import model.Partido;
import model.Usuario;

/**
 * Estado del partido cuando todos los jugadores han confirmado su asistencia
 * y el partido está listo para jugarse
 * Implementa el patrón State
 */
public class Confirmado implements EstadoPartido {
    
    @Override
    public boolean manejarNuevoJugador(Partido contexto, Usuario jugador) {
        // En este estado no se pueden agregar más jugadores
        // El partido ya está confirmado y listo
        return false;
    }
    
    @Override
    public boolean manejarConfirmacion(Partido contexto, Usuario jugador) {
        // Ya todos confirmaron, no hay más confirmaciones pendientes
        return false;
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
        
        // Verificar si es hora de que comience el partido
        long horasHastaPartido = contexto.getHorasHastaPartido();
        
        if (horasHastaPartido <= 0) {
            // Si ya es la hora del partido o ya pasó, cambiar a "En Juego"
            contexto.setEstado(new EnJuego());
            notificarEnJuego(contexto);
        }
        
        // Si algunos jugadores se retiran después de confirmar
        // y el partido queda incompleto, volver a estado anterior
        if (!contexto.estaCompleto()) {
            contexto.setEstado(new NecesitamosJugadores());
            notificarNecesitamosJugadores(contexto);
        }
    }
    
    @Override
    public String getNombre() {
        return "Confirmado";
    }
    
    @Override
    public boolean puedeAgregarJugadores() {
        return false;
    }
    
    @Override
    public boolean puedeCancelar() {
        return true; // Se puede cancelar hasta antes de empezar
    }
    
    @Override
    public boolean puedeConfirmar() {
        return false; // Ya está confirmado
    }
    
    /**
     * Permite que un jugador se retire del partido confirmado
     * @param contexto el partido del que se retira
     * @param jugador el jugador que se retira
     * @return true si se retiró exitosamente
     */
    public boolean manejarRetiro(Partido contexto, Usuario jugador) {
        if (contexto == null || jugador == null) {
            return false;
        }
        
        // En este estado, retirarse tiene consecuencias más serias
        // ya que el partido estaba confirmado
        boolean retirado = contexto.removerJugador(jugador);
        
        if (retirado) {
            // Verificar transición después del retiro
            verificarTransicion(contexto);
            notificarRetiroConfirmado(contexto, jugador);
        }
        
        return retirado;
    }
    
    /**
     * Verifica si el partido debe comenzar automáticamente
     * @param contexto el partido a verificar
     * @return true si debe comenzar, false en caso contrario
     */
    public boolean debeComenzar(Partido contexto) {
        if (contexto == null) {
            return false;
        }
        
        // El partido debe comenzar si llegó la hora
        return contexto.getHorasHastaPartido() <= 0;
    }
    
    /**
     * Obtiene el tiempo restante para que comience el partido
     * @param contexto el partido
     * @return minutos restantes hasta el inicio
     */
    public long getMinutosHastaInicio(Partido contexto) {
        if (contexto == null) {
            return 0;
        }
        
        return contexto.getHorasHastaPartido() * 60;
    }
    
    /**
     * Notifica que el partido fue cancelado
     * @param contexto el partido cancelado
     */
    private void notificarCancelacion(Partido contexto) {
        try {
            model.Notificacion notificacion = new model.Notificacion();
            notificacion.setIdPartido(contexto.getId());
            notificacion.setMensaje("ATENCIÓN: El partido confirmado ha sido cancelado");
            notificacion.setFechaCreacion(new java.util.Date());
            
            contexto.agregarNotificacion(notificacion);
        } catch (Exception e) {
            System.err.println("Error al notificar cancelación: " + e.getMessage());
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
            notificacion.setMensaje("¡El partido ha comenzado! ¡Que tengan un gran juego!");
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
            notificacion.setMensaje("URGENTE: Un jugador se retiró del partido confirmado. Necesitamos reemplazos.");
            notificacion.setFechaCreacion(new java.util.Date());
            
            contexto.agregarNotificacion(notificacion);
        } catch (Exception e) {
            System.err.println("Error al notificar necesidad de jugadores: " + e.getMessage());
        }
    }
    
    /**
     * Notifica que un jugador se retiró de un partido confirmado
     * @param contexto el partido
     * @param jugador el jugador que se retiró
     */
    private void notificarRetiroConfirmado(Partido contexto, Usuario jugador) {
        try {
            model.Notificacion notificacion = new model.Notificacion();
            notificacion.setIdPartido(contexto.getId());
            notificacion.setMensaje("IMPORTANTE: " + jugador.getNombre() + 
                                  " se retiró del partido ya confirmado. Esto afecta la organización.");
            notificacion.setFechaCreacion(new java.util.Date());
            
            contexto.agregarNotificacion(notificacion);
        } catch (Exception e) {
            System.err.println("Error al notificar retiro confirmado: " + e.getMessage());
        }
    }
    
    /**
     * Verifica si el partido está listo para comenzar
     * @param contexto el partido a verificar
     * @return true si está listo, false en caso contrario
     */
    public boolean estaListoParaComenzar(Partido contexto) {
        if (contexto == null) {
            return false;
        }
        
        return contexto.estaCompleto() && 
               contexto.esEnElFuturo() && 
               contexto.getHorasHastaPartido() <= 1; // Una hora antes como máximo
    }
    
    @Override
    public String toString() {
        return "Confirmado{" +
                "nombre='" + getNombre() + '\'' +
                '}';
    }
}