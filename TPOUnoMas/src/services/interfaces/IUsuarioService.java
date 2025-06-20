package services.interfaces;

import model.dto.UsuarioDTO;
import model.dto.DeporteUsuarioDTO;

import java.util.List;

public interface IUsuarioService {
    UsuarioDTO registrarUsuario(UsuarioDTO dto);
    UsuarioDTO buscarUsuario(String id);
    boolean actualizarPerfil(UsuarioDTO dto);
    void actualizarPreferencias(String usuarioId, List<DeporteUsuarioDTO> preferenciasDTO);
}
