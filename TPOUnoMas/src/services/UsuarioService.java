package services;

import enums.NivelJuego;
import mapper.UsuarioMapper;
import model.Deporte;
import model.DeporteUsuario;
import model.Usuario;
import model.dto.DeporteUsuarioDTO;
import model.dto.UsuarioDTO;
import repository.DeporteRepository;
import repository.UsuarioRepository;
import services.interfaces.IUsuarioService;

import java.util.ArrayList;
import java.util.List;

public class UsuarioService implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final DeporteRepository deporteRepository;

    public UsuarioService() {
        this.usuarioRepository = new UsuarioRepository();
        this.deporteRepository = new DeporteRepository();
    }

    public UsuarioDTO registrarUsuario(UsuarioDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El usuario no puede ser null");
        }

        Usuario usuario = UsuarioMapper.fromDto(dto);

        if (usuario.getId() == null || usuario.getId().trim().isEmpty()) {
            usuario.setId("USR_" + System.currentTimeMillis());
        }

        usuarioRepository.guardar(usuario);
        return UsuarioMapper.toDTO(usuario);
    }

    public UsuarioDTO buscarUsuario(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID no puede ser null o vacío");
        }

        Usuario usuario = usuarioRepository.buscarPorId(id);
        return usuario != null ? UsuarioMapper.toDTO(usuario) : null;
    }

    public boolean actualizarPerfil(UsuarioDTO dto) {
        if (dto == null || dto.getId() == null) {
            throw new IllegalArgumentException("UsuarioDTO inválido para actualizar");
        }

        Usuario usuario = UsuarioMapper.fromDto(dto);
        return usuarioRepository.actualizar(usuario);
    }

    public void actualizarPreferencias(String usuarioId, List<DeporteUsuarioDTO> preferenciasDTO) {
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado con ID: " + usuarioId);
        }

        List<DeporteUsuario> preferencias = new ArrayList<>();
        for (DeporteUsuarioDTO dto : preferenciasDTO) {
            Deporte deporte = deporteRepository.buscarPorId(dto.getDeporte().getId());
            NivelJuego nivel = dto.getNivel();

            if (deporte != null && nivel != null) {
                preferencias.add(new DeporteUsuario(deporte, nivel));
            }
        }

        usuario.setDeportesUsuario(preferencias);
        usuarioRepository.actualizar(usuario);
    }
}
