package repository.interfaces;

import model.dto.PartidoDTO;
import model.entity.Partido;

import java.util.List;

public interface IPartidoRepository {
    String guardar(Partido partido);
    List<Partido> obtenerTodos();
    Partido buscarPorId(String id);
    List<Partido> obtenerPartidosDelUsuario(String usuarioid);
}
