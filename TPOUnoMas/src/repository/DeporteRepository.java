package repository;

import model.Deporte;
import model.staticdb.DataBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class DeporteRepository {

    public List<Deporte> obtenerTodos() {
        return new ArrayList<>(DataBase.deportes);
    }

    public Optional<Deporte> buscarPorNombre(String nombre) {
        return DataBase.deportes.stream()
                .filter(d -> d.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }

    public Optional<Deporte> buscarPorId(String id) {
        return DataBase.deportes.stream()
                .filter(d -> d.getId().equals(id))
                .findFirst();
    }

    public void guardar(Deporte deporte) {
        DataBase.deportes.add(deporte);
    }

    public boolean eliminarPorNombre(String nombre) {
        return DataBase.deportes.removeIf(d -> d.getNombre().equalsIgnoreCase(nombre));
    }

    public boolean actualizar(Deporte deporteActualizado) {
        for (int i = 0; i < DataBase.deportes.size(); i++) {
            if (DataBase.deportes.get(i).getId().equals(deporteActualizado.getId())) {
                DataBase.deportes.set(i, deporteActualizado);
                return true;
            }
        }
        return false;
    }

    public List<String> obtenerNombresDisponibles() {
        return DataBase.deportes.stream()
                .map(Deporte::getNombre)
                .collect(Collectors.toList());
    }
}
