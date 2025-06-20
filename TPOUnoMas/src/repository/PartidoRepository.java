package repository;

import model.Partido;
import model.staticdb.DataBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PartidoRepository {

    public void guardar(Partido partido) {
        if (partido.getId() == null || partido.getId().isBlank()) {
            partido.setId(UUID.randomUUID().toString());
        }
        DataBase.partidos.add(partido);
    }

    public List<Partido> obtenerTodos() {
        return new ArrayList<>(DataBase.partidos);
    }

    public Optional<Partido> buscarPorId(String id) {
        return DataBase.partidos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

}
