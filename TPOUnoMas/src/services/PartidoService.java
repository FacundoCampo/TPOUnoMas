package services;

import mapper.PartidoMapper;
import model.Partido;
import model.dto.PartidoDTO;
import model.state.IEstadoPartido;
import repository.PartidoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PartidoService {

    private final PartidoRepository repository;

    public PartidoService() {
        this.repository = new PartidoRepository();
    }

    public void crearPartido(PartidoDTO dto) {
        Partido partido = PartidoMapper.fromDTO(dto);
        repository.guardar(partido);
    }

    public List<PartidoDTO> obtenerTodos() {
        List<Partido> lista = repository.obtenerTodos();
        List<PartidoDTO> resultado = new ArrayList<>();

        for (Partido p : lista) {
            resultado.add(PartidoMapper.toDTO(p));
        }

        return resultado;
    }

    public Optional<Partido> buscarPorID(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID no puede ser null o vacío");
        }

        return repository.buscarPorId(id);
    }

    public boolean cambiarEstado(String idPartido, IEstadoPartido estado) {
        Optional<Partido> partidoOpt = buscarPorID(idPartido);

        if (partidoOpt.isPresent()) {
            Partido partido = partidoOpt.get();
            partido.setEstado(estado);
            return true;
        }

        return false;
    }
}
