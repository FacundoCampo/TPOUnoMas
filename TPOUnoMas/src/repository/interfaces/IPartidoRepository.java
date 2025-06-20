package repository.interfaces;

import model.Partido;

import java.util.List;
import java.util.Optional;

public interface IPartidoRepository {
    void guardar(Partido partido);
    List<Partido> obtenerTodos();
    Partido buscarPorId(String id);
}
