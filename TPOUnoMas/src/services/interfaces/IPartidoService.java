package services.interfaces;

import enums.TipoEmparejamiento;
import model.dto.UsuarioDTO;
import model.entity.Partido;
import model.dto.PartidoDTO;
import model.estadosDelPartido.IEstadoPartido;

import java.util.List;

public interface IPartidoService {
    String crearPartido(PartidoDTO dto);
    List<PartidoDTO> obtenerTodos();
    Partido buscarPorID(String id);
    boolean cambiarEstado(String idPartido, IEstadoPartido estado);
    boolean sumarseAlPartido(String partidoid, String usuarioid);
    List<PartidoDTO> obtenerPartidosDelUsuario(String usuarioid);
    List<PartidoDTO> obtenerHistorial(String usuarioid);
    List<UsuarioDTO> emparejar(String partidoId);
    boolean cambiarTipoEmparejamiento(String id, TipoEmparejamiento nuevoTipo);
    List<PartidoDTO> obtenerSoloPartidosDondeSeNecesitanJugadores();
}
