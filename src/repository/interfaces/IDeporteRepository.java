package repository.interfaces;

import model.entity.Deporte;

import java.util.List;
import java.util.Optional;

public interface IDeporteRepository {
    List<Deporte> obtenerTodos();
    Optional<Deporte> buscarPorNombre(String nombre);
    void guardar(Deporte deporte);
    boolean eliminarPorNombre(String nombre);
    boolean actualizar(Deporte deporteActualizado);
    List<String> obtenerNombresDisponibles();
    Deporte buscarPorId(String id);
}
