package controller;

import model.dto.DeporteUsuarioDTO;
import model.dto.UsuarioDTO;
import services.UsuarioService;
import services.interfaces.IUsuarioService;

import java.util.List;

public class UsuarioController {

    private static UsuarioController instance;
    private IUsuarioService usuarioService;

    private UsuarioController() {
        this.usuarioService = new UsuarioService();
    }

    public static UsuarioController getInstance() {
        if (instance == null) {
            instance = new UsuarioController();
        }
        return instance;
    }

    public boolean registrar(UsuarioDTO usuario) {
        UsuarioDTO u = usuarioService.registrarUsuario(usuario);
        if(u != null) return true;
        return false;
    }

    public UsuarioDTO buscarPorId(String id) {
        return usuarioService.buscarUsuario(id);
    }

    public void actualizarPerfil(UsuarioDTO usuario) {
        usuarioService.actualizarPerfil(usuario);
    }

    public void actualizarPreferencias(String usuarioId, List<DeporteUsuarioDTO> preferenciasDTO) {
        usuarioService.actualizarPreferencias(usuarioId, preferenciasDTO);
    }

}
