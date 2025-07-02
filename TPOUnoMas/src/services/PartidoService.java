package services;

import enums.TipoEmparejamiento;
import mapper.PartidoMapper;
import mapper.UsuarioMapper;
import model.dto.UsuarioDTO;
import model.entity.Partido;
import model.entity.PartidoContext;
import model.entity.Usuario;
import model.dto.PartidoDTO;
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

    private Partido buscarPorID(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID no puede ser null o vacío");
        }
        Partido p = repository.buscarPorId(id);

        return repository.buscarPorId(id);
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
        if (partido == null) return false;

        PartidoContext contexto = new PartidoContext(partido);

        if (contexto.permiteCambioDeEstrategia()) {
            partido.setTipoEmparejamiento(nuevoTipo);
            return true;
        }

        return false;
    }


    public boolean agregarJugador(String partidoId, String jugador) {
        Partido partido = repository.buscarPorId(partidoId);
        PartidoContext contexto = new PartidoContext(partido);
        Usuario u = usuarioRepository.buscarPorEmail(jugador);

        try {
            contexto.agregarJugador(u);
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    public boolean cancelarPartido(String partidoId, String motivo) {
        Partido partido = repository.buscarPorId(partidoId);
        PartidoContext contexto = new PartidoContext(partido);

        try {
            contexto.cancelar(motivo);
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    public boolean iniciarPartidoSiCorresponde(String partidoId) {
        Partido partido = repository.buscarPorId(partidoId);
        PartidoContext contexto = new PartidoContext(partido);

        try {
            contexto.finalizar();
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    public boolean finalizarPartido(String partidoId) {
        Partido partido = repository.buscarPorId(partidoId);
        PartidoContext contexto = new PartidoContext(partido);

        try {
            contexto.finalizar();
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

}
