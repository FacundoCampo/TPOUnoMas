package repository;

import model.dto.PartidoDTO;
import model.entity.Partido;
import model.staticdb.DataBase;
import repository.interfaces.IPartidoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PartidoRepository implements IPartidoRepository {

    public String guardar(Partido partido) {
        if (partido.getId() == null || partido.getId().isBlank()) {
            partido.setId(UUID.randomUUID().toString());
        }
        DataBase.partidos.add(partido);
        return partido.getId();
    }

    public List<Partido> obtenerTodos() {
        return new ArrayList<>(DataBase.partidos);
    }

    public Partido buscarPorId(String id) {
        for (Partido u : DataBase.partidos) {
            if (u.getId().equals(id)) {
                return u;
            }
        }
        return null;
    }

    public List<Partido> obtenerPartidosDelUsuario(String usuarioid) {
        List<Partido> p = new ArrayList<>();

        for (Partido partido : DataBase.partidos) {
            if (partido.getOrganizadorID().toLowerCase().equals(usuarioid.toLowerCase())) {
                p.add(partido);
            }
        }

        return p;
    }

}
