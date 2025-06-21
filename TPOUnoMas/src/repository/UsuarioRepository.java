package repository;

import model.DeporteUsuario;
import model.Usuario;
import model.dto.DeporteUsuarioDTO;
import model.staticdb.DataBase;
import repository.interfaces.IUsuarioRepository;

import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository implements IUsuarioRepository {

    public List<Usuario> obtenerTodos() {
        return new ArrayList<>(DataBase.usuarios);
    }

    public Usuario buscarPorEmail(String email) {
        for (Usuario u : DataBase.usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }

    public void guardar(Usuario usuario) {
        DataBase.usuarios.add(usuario);
    }

    public boolean actualizar(Usuario usuarioActualizado) {
        for (int i = 0; i < DataBase.usuarios.size(); i++) {
            if (DataBase.usuarios.get(i).getEmail().equals(usuarioActualizado.getEmail())) {
                DataBase.usuarios.set(i, usuarioActualizado);
                return true;
            }
        }
        return false;
    }

    public List<DeporteUsuario> obtenerPreferencias(String id) {
        Usuario u = buscarPorEmail(id);
        return u.getDeportesUsuario();
    }
}
