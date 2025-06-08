package state;

import model.Partido;
import model.Usuario;

/**
 * Estado que representa un partido que ha finalizado
 * Implementa el patrón State
 * Este es un estado terminal - no se puede transicionar a otros estados
 */
public class Finalizado implements EstadoPartido {
    
    @Override
    public boolean manejarNuevoJugador(Partido contexto, Usuario jugador) {
        // No se pueden agregar jugadores a un partido finalizado
        System.out.println("No se pueden agregar jugadores: el partido ya ha finalizado");
        return false;
    }
    
    @Override
    public boolean manejarConfirmacion(Partido contexto, Usuario jugador) {
        // No se pueden confirmar jugadores en un partido finalizado
        System.out.println("No se puede confirmar participación: el partido ya ha finalizado");
        return false;
    }
    
    @Override
    public void manejarCancelacion(Partido contexto) {
        // No se puede cancelar un partido que ya finalizó
        System.out.println("No se puede cancelar un partido que ya ha finalizado");
    }
    
    @Override
    public void verificarTransicion(Partido contexto) {
        // Estado terminal - no hay transiciones desde aquí
        // Solo se pueden realizar operaciones de consulta o estadísticas
        System.out.println("Partido finalizado - no hay transiciones de estado disponibles");
    }
    
    @Override
    public String getNombre() {
        return "Finalizado";
    }
    
    @Override
    public boolean puedeAgregarJugadores() {
        return false;
    }
    
    @Override
    public boolean puedeCancelar() {
        return false;
    }
    
    @Override
    public boolean puedeConfirmar() {
        return false;
    }
    
    /**
     * Verifica si se pueden agregar comentarios o estadísticas al partido
     * @return true ya que los partidos finalizados pueden tener estadísticas
     */
    public boolean puedeAgregarEstadisticas() {
        return true;
    }
    
    /**
     * Verifica si se puede acceder al historial del partido
     * @return true ya que los partidos finalizados forman parte del historial
     */
    public boolean puedeAccederHistorial() {
        return true;
    }
    
    /**
     * Verifica si el partido está disponible para generar reportes
     * @return true ya que los partidos finalizados pueden ser incluidos en reportes
     */
    public boolean puedeGenerarReportes() {
        return true;
    }
    
    /**
     * Obtiene información del resultado del partido
     * @param contexto el partido finalizado
     * @return string con información del resultado
     */
    public String getInformacionResultado(Partido contexto) {
        StringBuilder resultado = new StringBuilder();
        resultado.append("Partido finalizado: ");
        
        if (contexto.getDeporte() != null) {
            resultado.append(contexto.getDeporte().getNombre());
        }
        
        resultado.append("\nUbicación: ").append(contexto.getUbicacion());
        resultado.append("\nFecha: ").append(contexto.getFechaHora());
        resultado.append("\nDuración: ").append(contexto.getDuracion()).append(" minutos");
        resultado.append("\nJugadores participantes: ").append(contexto.getJugadoresInscritos().size());
        
        return resultado.toString();
    }
    
    /**
     * Verifica si todos los jugadores completaron el partido
     * @param contexto el partido a verificar
     * @return true si todos los jugadores inscritos participaron
     */
    public boolean todosLosJugadoresParticiparon(Partido contexto) {
        // En esta implementación asumimos que todos los jugadores inscritos participaron
        // En una implementación más compleja, se podría verificar la asistencia real
        return !contexto.getJugadoresInscritos().isEmpty();
    }
    
    @Override
    public String toString() {
        return "Estado: " + getNombre();
    }
}