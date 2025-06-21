package services.interfaces;

import model.entity.Partido;
import model.dto.PartidoDTO;
import model.estadosDelPartido.IEstadoPartido;

import java.util.List;

public interface IPartidoService {
    void crearPartido(PartidoDTO dto);
    List<PartidoDTO> obtenerTodos();
    Partido buscarPorID(String id);
    boolean cambiarEstado(String idPartido, IEstadoPartido estado);
    boolean sumarseAlPartido(String partidoid, String usuarioid);
}
