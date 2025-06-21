package strategy;

import model.entity.Partido;
import model.entity.Usuario;
import java.util.List;

public interface IEstrategiaEmparejamiento {
    List<Usuario> emparejarJugadores(Partido partido, List<Usuario> candidatos);
    boolean esJugadorApto(Partido partido, Usuario jugador);
}