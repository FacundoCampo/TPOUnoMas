package model.estadosDelPartido;

import model.Partido;
import model.Usuario;

public class EnJuego implements IEstadoPartido {
    
    @Override
    public boolean manejarNuevoJugador(Partido contexto, Usuario jugador) {
        return false; // No se pueden agregar jugadores durante el juego
    }
    
    @Override
    public boolean manejarConfirmacion(Partido contexto, Usuario jugador) {
        return false; // No se pueden confirmar jugadores durante el juego
    }
    
    @Override
    public void manejarCancelacion(Partido contexto) {
        System.out.println("No se puede cancelar un partido que está en juego. Debe finalizarse.");
    }
    
    @Override
    public void verificarTransicion(Partido contexto) {
        if (debeFinalizarse(contexto)) {
            contexto.setEstado(new Finalizado());
            for (Usuario jugador : contexto.getJugadoresInscritos()) {
                jugador.agregarPartidoAHistorial(contexto);
            }
        }
    }
    
    private boolean debeFinalizarse(Partido contexto) {
        if (contexto.getFechaHora() == null) return false;
        
        long tiempoActual = System.currentTimeMillis();
        long tiempoInicio = contexto.getFechaHora().getTime();
        long duracionMs = contexto.getDuracion() * 60 * 1000L;
        long tiempoFin = tiempoInicio + duracionMs;
        
        return tiempoActual >= tiempoFin;
    }
    
    @Override
    public String getNombre() { return "En juego"; }
}