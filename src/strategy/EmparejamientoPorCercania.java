package strategy;

import model.entity.Partido;
import model.entity.Usuario;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EmparejamientoPorCercania implements IEstrategiaEmparejamiento {
    private int radioKilometros;
    
    public EmparejamientoPorCercania() {
        this.radioKilometros = 10;
    }
    
    public EmparejamientoPorCercania(int radioKilometros) {
        this.radioKilometros = radioKilometros;
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

        aptos.sort(new Comparator<Usuario>() {
            @Override
            public int compare(Usuario u1, Usuario u2) {
                double d1 = calcularDistancia(partido.getUbicacion(), u1.getUbicacion());
                double d2 = calcularDistancia(partido.getUbicacion(), u2.getUbicacion());
                return Double.compare(d1, d2);
            }
        });

        return aptos;
    }
    
    @Override
    public boolean esJugadorApto(Partido partido, Usuario jugador) {
        if (partido == null || jugador == null) return false;
        if (partido.getUbicacion() == null || jugador.getUbicacion() == null) return false;
        if (partido.getJugadoresInscritos().contains(jugador)) return false;
        if (jugador.getNivelJuegoParaDeporte(partido.getDeporte()) == null) return false;

        double distancia = calcularDistancia(partido.getUbicacion(), jugador.getUbicacion());
        return distancia <= radioKilometros;
    }
    
    public double calcularDistancia(String ubicacionPartido, String ubicacionJugador) {
        // Implementación simplificada para demo
        if (ubicacionPartido == null || ubicacionJugador == null) return Double.MAX_VALUE;
        
        String ub1 = ubicacionPartido.toLowerCase();
        String ub2 = ubicacionJugador.toLowerCase();
        
        if (ub1.equals(ub2)) return 0.0;
        if (ub1.contains("palermo") && ub2.contains("recoleta")) return 3.0;
        if (ub1.contains("recoleta") && ub2.contains("belgrano")) return 5.0;
        
        return Math.random() * 20; // Distancia aleatoria para demo
    }
    
    public int getRadioKilometros() { return radioKilometros; }
    public void setRadioKilometros(int radioKilometros) { this.radioKilometros = radioKilometros; }
}