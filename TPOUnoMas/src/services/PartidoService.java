package services;

import mapper.PartidoMapper;
import model.Partido;
import model.Usuario;
import model.dto.PartidoDTO;
import model.state.IEstadoPartido;
import repository.PartidoRepository;
import repository.UsuarioRepository;
import services.interfaces.IPartidoService;

import java.util.ArrayList;
import java.util.List;

public class PartidoService implements IPartidoService {

    private PartidoRepository repository;
    private UsuarioRepository usuarioRepository;

    public PartidoService() {
        this.repository = new PartidoRepository();
        this.usuarioRepository = new UsuarioRepository();
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

    public Partido buscarPorID(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID no puede ser null o vacío");
        }
        Partido p = repository.buscarPorId(id);

        return repository.buscarPorId(id);
    }

    public boolean cambiarEstado(String idPartido, IEstadoPartido estado) {
        Partido partido = buscarPorID(idPartido);

        if (partido != null) {
            partido.setEstado(estado);
            return true;
        }

        return false;
    }

    public boolean sumarseAlPartido(String partidoid, String usuarioid) {
        Partido partido = repository.buscarPorId(partidoid);
        Usuario usuario = usuarioRepository.buscarPorId(usuarioid);
        return partido.getEstado().manejarNuevoJugador(partido, usuario);
    }
}
