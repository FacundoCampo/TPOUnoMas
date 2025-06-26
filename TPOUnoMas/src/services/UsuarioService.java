package services;

import enums.NivelJuego;
import mapper.DeporteUsuarioMapper;
import mapper.UsuarioMapper;
import model.entity.Deporte;
import model.entity.DeporteUsuario;
import model.entity.Usuario;
import model.dto.DeporteUsuarioDTO;
import model.dto.UsuarioDTO;
import repository.DeporteRepository;
import repository.UsuarioRepository;
import repository.interfaces.IDeporteRepository;
import repository.interfaces.IUsuarioRepository;
import services.interfaces.IUsuarioService;

import java.util.ArrayList;
import java.util.List;

public class UsuarioService implements IUsuarioService {

    private IUsuarioRepository usuarioRepository;
    private IDeporteRepository deporteRepository;

    public UsuarioService() {
        this.usuarioRepository = new UsuarioRepository();
        this.deporteRepository = new DeporteRepository();
    }

    public UsuarioDTO registrarUsuario(UsuarioDTO dto) throws Exception {
        if (dto == null) {
            throw new IllegalArgumentException("El usuario no puede ser null");
        }

        if(ExisteUsuario(dto)) throw new Exception("Ya existe usuario con ese email");

        Usuario usuario = UsuarioMapper.fromDto(dto);

        usuarioRepository.guardar(usuario);
        return UsuarioMapper.toDTO(usuario);
    }


    public UsuarioDTO buscarUsuario(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID no puede ser null o vacío");
        }

        Usuario usuario = usuarioRepository.buscarPorEmail(id);
        return usuario != null ? UsuarioMapper.toDTO(usuario) : null;
    }

    public boolean actualizarPerfil(UsuarioDTO dto) {
        if (dto == null || dto.getEmail() == null) {
            throw new IllegalArgumentException("UsuarioDTO inválido para actualizar");
        }

        Usuario usuario = UsuarioMapper.fromDto(dto);
        return usuarioRepository.actualizar(usuario);
    }

    public void actualizarPreferencias(String usuarioId, List<DeporteUsuarioDTO> preferenciasDTO) {
        Usuario usuario = usuarioRepository.buscarPorEmail(usuarioId);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado con ID: " + usuarioId);
        }

        List<DeporteUsuario> preferencias = new ArrayList<>();
        for (DeporteUsuarioDTO dto : preferenciasDTO) {
            Deporte deporte = deporteRepository.buscarPorId(dto.getDeporte().getId());
            NivelJuego nivel = dto.getNivel();
            boolean esFav = dto.esFavorito();

            if (deporte != null && nivel != null) {
                preferencias.add(new DeporteUsuario(deporte, nivel, esFav));
            }
        }

        usuario.setDeportesUsuario(preferencias);
    }

    public UsuarioDTO login(String email, String contrasena) {
        if (email == null || email.trim().isEmpty() || contrasena == null || contrasena.trim().isEmpty()) {
            throw new IllegalArgumentException("Email y password son obligatorios");
        }

        Usuario usuario = usuarioRepository.buscarPorEmail(email.trim().toLowerCase());

        if (usuario == null || !usuario.getContraseña().equals(contrasena)) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        return UsuarioMapper.toDTO(usuario);
    }

    public List<UsuarioDTO> obtenerTodos() {
        List<Usuario> usuarios = usuarioRepository.obtenerTodos();
        List<UsuarioDTO> resultado = new ArrayList<>();

        for (Usuario usuario : usuarios) {
            UsuarioDTO dto = UsuarioMapper.toDTO(usuario);
            resultado.add(dto);
        }

        return resultado;
    }



    private boolean ExisteUsuario(UsuarioDTO dto) {
        Usuario existente = usuarioRepository.buscarPorEmail(dto.getEmail());
        return existente != null;
    }

    public List<DeporteUsuarioDTO> obtenerPreferencias(String id) {
        List<DeporteUsuario> du = usuarioRepository.obtenerPreferencias(id);
        List<DeporteUsuarioDTO> dtoList = new ArrayList<>();

        if (du != null) {
            for (DeporteUsuario l : du) {
                dtoList.add(DeporteUsuarioMapper.toDTO(l));
            }
        }
        return dtoList;
    }

}
