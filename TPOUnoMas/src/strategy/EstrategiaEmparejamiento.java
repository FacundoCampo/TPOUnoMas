package strategy;

import model.Partido;
import model.Usuario;
import java.util.List;

public interface EstrategiaEmparejamiento {
    List<Usuario> emparejarJugadores(Partido partido, List<Usuario> candidatos);
    boolean esJugadorApto(Partido partido, Usuario jugador);
}