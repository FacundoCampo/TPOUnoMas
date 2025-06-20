package repository;

import model.Usuario;
import model.staticdb.DataBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UsuarioRepository {

    public List<Usuario> obtenerTodos() {
        return new ArrayList<>(DataBase.usuarios);
    }

    public Usuario buscarPorId(String id) {
        for (Usuario u : DataBase.usuarios) {
            if (u.getId().equals(id)) {
                return u;
            }
        }
        return null;
    }

    public Optional<Usuario> buscarPorNombre(String nombre) {
        return DataBase.usuarios.stream()
                .filter(u -> u.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }

    public void guardar(Usuario usuario) {
        DataBase.usuarios.add(usuario);
    }

    public boolean actualizar(Usuario usuarioActualizado) {
        for (int i = 0; i < DataBase.usuarios.size(); i++) {
            if (DataBase.usuarios.get(i).getId().equals(usuarioActualizado.getId())) {
                DataBase.usuarios.set(i, usuarioActualizado);
                return true;
            }
        }
        return false;
    }

    public List<String> obtenerNombresDisponibles() {
        return DataBase.usuarios.stream()
                .map(Usuario::getNombre)
                .collect(Collectors.toList());
    }
}
