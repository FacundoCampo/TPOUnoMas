package state;

import model.Partido;
import model.Usuario;

/**
 * Estado que representa un partido que está actualmente en juego
 * Implementa el patrón State
 */
public class EnJuego implements EstadoPartido {
    
    @Override
    public boolean manejarNuevoJugador(Partido contexto, Usuario jugador) {
        // No se pueden agregar jugadores cuando el partido está en juego
        System.out.println("No se pueden agregar jugadores: el partido está en juego");
        return false;
    }
    
    @Override
    public boolean manejarConfirmacion(Partido contexto, Usuario jugador) {
        // No se pueden confirmar jugadores cuando el partido ya está en juego
        System.out.println("No se puede confirmar participación: el partido ya está en juego");
        return false;
    }
    
    @Override
    public void manejarCancelacion(Partido contexto) {
        // Un partido en juego no se puede cancelar, solo finalizar
        System.out.println("No se puede cancelar un partido que está en juego. Debe finalizarse.");
    }
    
    @Override
    public void verificarTransicion(Partido contexto) {
        // Verificar si el partido debe transicionar a Finalizado
        // Esto podría ser por tiempo transcurrido o por finalización manual
        if (debeFinalizarse(contexto)) {
            System.out.println("Transicionando partido a estado Finalizado");
            contexto.setEstado(new Finalizado());
            
            // Agregar los jugadores al historial
            for (Usuario jugador : contexto.getJugadoresInscritos()) {
                jugador.agregarPartidoAHistorial(contexto);
            }
        }
    }
    
    /**
     * Verifica si el partido debe finalizarse
     * @param contexto el partido a verificar
     * @return true si debe finalizarse, false en caso contrario
     */
    private boolean debeFinalizarse(Partido contexto) {
        if (contexto.getFechaHora() == null) {
            return false;
        }
        
        // Calcular si ha pasado el tiempo del partido (fecha + duración)
        long tiempoActual = System.currentTimeMillis();
        long tiempoInicio = contexto.getFechaHora().getTime();
        long duracionMs = contexto.getDuracion() * 60 * 1000; // convertir minutos a milisegundos
        long tiempoFin = tiempoInicio + duracionMs;
        
        return tiempoActual >= tiempoFin;
    }
    
    @Override
    public String getNombre() {
        return "En juego";
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
     * Finaliza manualmente el partido
     * @param contexto el partido a finalizar
     */
    public void finalizarPartido(Partido contexto) {
        System.out.println("Finalizando partido manualmente");
        contexto.setEstado(new Finalizado());
        
        // Agregar los jugadores al historial
        for (Usuario jugador : contexto.getJugadoresInscritos()) {
            jugador.agregarPartidoAHistorial(contexto);
        }
    }
    
    /**
     * Verifica si el partido puede ser finalizado manualmente
     * @return true ya que un partido en juego siempre puede ser finalizado
     */
    public boolean puedeFinalizarse() {
        return true;
    }
    
    @Override
    public String toString() {
        return "Estado: " + getNombre();
    }
}