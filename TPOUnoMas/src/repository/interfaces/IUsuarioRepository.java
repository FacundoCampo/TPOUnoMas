package repository.interfaces;

import model.entity.DeporteUsuario;
import model.entity.Usuario;

import java.util.List;

public interface IUsuarioRepository {
    List<Usuario> obtenerTodos();
    void guardar(Usuario usuario);
    boolean actualizar(Usuario usuarioActualizado);
    Usuario buscarPorEmail(String email);
    List<DeporteUsuario> obtenerPreferencias(String id);
}
