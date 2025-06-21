package services.interfaces;

import model.entity.Usuario;
import model.dto.UsuarioDTO;
import model.dto.DeporteUsuarioDTO;

import java.util.List;

public interface IUsuarioService {
    UsuarioDTO registrarUsuario(UsuarioDTO dto) throws Exception;
    UsuarioDTO buscarUsuario(String id);
    boolean actualizarPerfil(UsuarioDTO dto);
    void actualizarPreferencias(String usuarioId, List<DeporteUsuarioDTO> preferenciasDTO);
    UsuarioDTO login(String email, String contrasena);
    List<Usuario> obtenerTodos();
    List<DeporteUsuarioDTO> obtenerPreferencias(String id);
}
