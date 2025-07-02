package services.interfaces;

import enums.TipoEmparejamiento;
import model.dto.UsuarioDTO;
import model.entity.Partido;
import model.dto.PartidoDTO;

import java.util.List;

public interface IPartidoService {
    String crearPartido(PartidoDTO dto);

    List<PartidoDTO> obtenerTodos();

    List<PartidoDTO> obtenerSoloPartidosDondeSeNecesitanJugadores();

    List<PartidoDTO> obtenerPartidosDelUsuario(String usuarioid);

    List<PartidoDTO> obtenerHistorial(String usuarioid);

    List<UsuarioDTO> emparejar(String partidoId);

    boolean cambiarTipoEmparejamiento(String id, TipoEmparejamiento nuevoTipo);

    boolean agregarJugador(String partidoId, String jugador);

    boolean cancelarPartido(String partidoId, String motivo);

    boolean finalizarPartido(String partidoId);
}
