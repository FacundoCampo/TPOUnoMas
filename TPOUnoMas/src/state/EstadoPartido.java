package state;

import model.Partido;
import model.Usuario;

/**
 * Interface que define el comportamiento común de todos los estados de un partido
 * Implementa el patrón State para gestionar las transiciones de estado de los partidos
 */
public interface EstadoPartido {
    
    /**
     * Maneja la lógica cuando un nuevo jugador intenta unirse al partido
     * @param contexto el partido al que se intenta unir el jugador
     * @param jugador el usuario que quiere unirse
     * @return true si el jugador se unió exitosamente, false en caso contrario
     */
    boolean manejarNuevoJugador(Partido contexto, Usuario jugador);
    
    /**
     * Maneja la confirmación de asistencia de un jugador
     * @param contexto el partido donde se confirma la asistencia
     * @param jugador el usuario que confirma su asistencia
     * @return true si la confirmación fue exitosa, false en caso contrario
     */
    boolean manejarConfirmacion(Partido contexto, Usuario jugador);
    
    /**
     * Maneja la cancelación del partido
     * @param contexto el partido que se va a cancelar
     */
    void manejarCancelacion(Partido contexto);
    
    /**
     * Verifica si el partido debe transicionar a otro estado
     * y realiza la transición si es necesario
     * @param contexto el partido a verificar
     */
    void verificarTransicion(Partido contexto);
    
    /**
     * Obtiene el nombre del estado actual
     * @return string con el nombre del estado
     */
    String getNombre();
    
    /**
     * Verifica si en este estado se pueden agregar más jugadores
     * @return true si se pueden agregar jugadores, false en caso contrario
     */
    default boolean puedeAgregarJugadores() {
        return false;
    }
    
    /**
     * Verifica si en este estado se puede cancelar el partido
     * @return true si se puede cancelar, false en caso contrario
     */
    default boolean puedeCancelar() {
        return false;
    }
    
    /**
     * Verifica si en este estado se pueden confirmar jugadores
     * @return true si se pueden confirmar jugadores, false en caso contrario
     */
    default boolean puedeConfirmar() {
        return false;
    }
}