package repository.interfaces;

import model.entity.Partido;

import java.util.List;

public interface IPartidoRepository {
    void guardar(Partido partido);
    List<Partido> obtenerTodos();
    Partido buscarPorId(String id);
}
