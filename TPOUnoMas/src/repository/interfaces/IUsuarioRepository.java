package repository.interfaces;

import model.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioRepository {
    List<Usuario> obtenerTodos();
    Usuario buscarPorId(String id);
    Optional<Usuario> buscarPorNombre(String nombre);
    void guardar(Usuario usuario);
    boolean actualizar(Usuario usuarioActualizado);
    List<String> obtenerNombresDisponibles();
}
