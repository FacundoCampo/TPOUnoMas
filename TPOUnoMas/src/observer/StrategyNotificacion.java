package observer;

import model.Partido;
import model.Usuario;
import java.util.List;

public interface StrategyNotificacion {
    void notificarNuevoPartido(Partido partido, List<Usuario> usuarios);
    void notificarCambioEstado(Partido partido, String estadoAnterior);
}