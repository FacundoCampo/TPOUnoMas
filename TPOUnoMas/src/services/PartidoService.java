package services;

import enums.TipoEmparejamiento;
import mapper.PartidoMapper;
import mapper.UsuarioMapper;
import model.dto.UsuarioDTO;
import model.entity.Partido;
import model.entity.Usuario;
import model.dto.PartidoDTO;
import model.estadosDelPartido.IEstadoPartido;
import repository.PartidoRepository;
import repository.UsuarioRepository;
import repository.interfaces.IPartidoRepository;
import repository.interfaces.IUsuarioRepository;
import services.interfaces.IPartidoService;
import strategy.EmparejamientoContext;
import strategy.EmparejamientoStrategyFactory;

import java.util.ArrayList;
import java.util.List;

public class PartidoService implements IPartidoService {

    private IPartidoRepository repository;
    private IUsuarioRepository usuarioRepository;

    public PartidoService() {
        this.repository = new PartidoRepository();
        this.usuarioRepository = new UsuarioRepository();
    }

    public String crearPartido(PartidoDTO dto) {
        Partido partido = PartidoMapper.fromDTO(dto);
        return repository.guardar(partido);
    }

    public List<PartidoDTO> obtenerTodos() {
        List<Partido> lista = repository.obtenerTodos();
        List<PartidoDTO> resultado = new ArrayList<>();

        for (Partido p : lista) {
            resultado.add(PartidoMapper.toDTO(p));
        }

        return resultado;
    }

    public List<PartidoDTO> obtenerSoloPartidosDondeSeNecesitanJugadores() {
        List<Partido> lista = repository.obtenerSoloPartidosDondeSeNecesitanJugadores();
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

    public boolean cambiarEstado(String idPartido, IEstadoPartido nuevoEstado) {
        Partido partido = buscarPorID(idPartido);

        if (partido != null && partido.getEstado().permiteTransicionA(nuevoEstado)) {
            partido.setEstado(nuevoEstado);
            return true;
        }

        return false;
    }


    public boolean sumarseAlPartido(String partidoid, String usuarioid) {
        Partido partido = repository.buscarPorId(partidoid);
        Usuario usuario = usuarioRepository.buscarPorEmail(usuarioid);
        return partido.getEstado().manejarNuevoJugador(partido, usuario);
    }

    public List<PartidoDTO> obtenerPartidosDelUsuario(String usuarioid) {
        List<Partido> lista = repository.obtenerPartidosDelUsuario(usuarioid);
        List<PartidoDTO> resultado = new ArrayList<>();

        for (Partido p : lista) {
            resultado.add(PartidoMapper.toDTO(p));
        }

        return resultado;
    }

    public List<PartidoDTO> obtenerHistorial(String usuarioid) {
        List<Partido> lista = repository.obtenerHistorial(usuarioid);
        List<PartidoDTO> resultado = new ArrayList<>();

        for (Partido p : lista) {
            resultado.add(PartidoMapper.toDTO(p));
        }

        return resultado;
    }

    public List<UsuarioDTO> emparejar(String partidoId) {
        List<UsuarioDTO> lista = new ArrayList<>();
        Partido partido = buscarPorID(partidoId);
        if (partido != null) {
            EmparejamientoContext ctx = new EmparejamientoContext(EmparejamientoStrategyFactory.crear(partido.getTipoEmparejamiento()));
            List<Usuario> todos = usuarioRepository.obtenerTodos();
            List<Usuario> u = ctx.emparejarJugadores(partido, todos);

            for (Usuario usuario : u) {
                UsuarioDTO dto = UsuarioMapper.toDTO(usuario);
                lista.add(dto);
            }
        }

        return lista;
    }

    public boolean cambiarTipoEmparejamiento(String id, TipoEmparejamiento nuevoTipo) {
        Partido partido = buscarPorID(id);
        if (partido != null && partido.getEstado().permiteCambioDeEstrategia()) {
            partido.setTipoEmparejamiento(nuevoTipo);
            return true;
        }
        return false;
    }

}
