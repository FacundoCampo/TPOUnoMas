package strategy;

import model.Partido;
import model.Usuario;
import java.util.List;
import java.util.stream.Collectors;

public class EmparejamientoPorHistorial implements IEstrategiaEmparejamiento {
    private int minimoPartidosRequeridos;
    
    public EmparejamientoPorHistorial() {
        this.minimoPartidosRequeridos = 0;
    }
    
    public EmparejamientoPorHistorial(int minimoPartidosRequeridos) {
        this.minimoPartidosRequeridos = minimoPartidosRequeridos;
    }
    
    @Override
    public List<Usuario> emparejarJugadores(Partido partido, List<Usuario> candidatos) {
        if (partido == null || candidatos == null) {
            throw new IllegalArgumentException("Parámetros no pueden ser null");
        }
        
        return candidatos.stream()
            .filter(candidato -> esJugadorApto(partido, candidato))
            .sorted((u1, u2) -> Integer.compare(u2.getCantidadPartidosJugados(), u1.getCantidadPartidosJugados()))
            .collect(Collectors.toList());
    }
    
    @Override
    public boolean esJugadorApto(Partido partido, Usuario jugador) {
        if (partido == null || jugador == null) return false;
        if (partido.getJugadoresInscritos().contains(jugador)) return false;
        if (jugador.getNivelJuegoParaDeporte(partido.getDeporte()) == null) return false;
        
        return jugador.getCantidadPartidosJugados() >= minimoPartidosRequeridos;
    }
    
    public int getMinimoPartidosRequeridos() { return minimoPartidosRequeridos; }
    public void setMinimoPartidosRequeridos(int minimoPartidosRequeridos) { 
        this.minimoPartidosRequeridos = minimoPartidosRequeridos; 
    }
}