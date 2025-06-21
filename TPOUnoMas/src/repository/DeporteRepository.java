package repository;

import model.entity.Deporte;
import model.staticdb.DataBase;
import repository.interfaces.IDeporteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class DeporteRepository implements IDeporteRepository {

    public List<Deporte> obtenerTodos() {
        return new ArrayList<>(DataBase.deportes);
    }

    public Optional<Deporte> buscarPorNombre(String nombre) {
        return DataBase.deportes.stream()
                .filter(d -> d.getNombre().equalsIgnoreCase(nombre))
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

    public Deporte buscarPorId(String id) {
        for (Deporte u : DataBase.deportes) {
            if (u.getId().equals(id)) {
                return u;
            }
        }
        return null;
    }
}
