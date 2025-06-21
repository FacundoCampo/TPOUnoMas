package repository.interfaces;

import model.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioRepository {
    List<Usuario> obtenerTodos();
    void guardar(Usuario usuario);
    boolean actualizar(Usuario usuarioActualizado);
    Usuario buscarPorEmail(String email);
}
