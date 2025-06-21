package services.notificacionService;

import model.entity.Partido;
import model.entity.Usuario;
import java.util.List;

public interface StrategyNotificacion {
    void notificarNuevoPartido(Partido partido, List<Usuario> usuarios);
    void notificarCambioEstado(Partido partido, String estadoAnterior);
}