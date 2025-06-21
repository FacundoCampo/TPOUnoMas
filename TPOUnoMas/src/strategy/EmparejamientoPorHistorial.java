package strategy;

import model.entity.Partido;
import model.entity.Usuario;

import java.util.ArrayList;
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

        List<Usuario> aptos = new ArrayList<>();

        for (Usuario candidato : candidatos) {
            if (esJugadorApto(partido, candidato)) {
                aptos.add(candidato);
            }
        }

        aptos.sort((u1, u2) -> Integer.compare(u2.getCantidadPartidosJugados(), u1.getCantidadPartidosJugados()));

        return aptos;
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