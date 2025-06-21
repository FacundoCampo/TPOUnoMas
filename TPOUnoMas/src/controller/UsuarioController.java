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

    public boolean registrar(UsuarioDTO usuario) throws Exception {
        UsuarioDTO u = usuarioService.registrarUsuario(usuario);
        return u != null;
    }

    public UsuarioDTO buscarPorId(String id) {
        return usuarioService.buscarUsuario(id);
    }

    public void actualizarPreferencias(String usuarioId, List<DeporteUsuarioDTO> preferenciasDTO) {
        usuarioService.actualizarPreferencias(usuarioId, preferenciasDTO);
    }

    public boolean login(String email, String contrasena) {
        UsuarioDTO u = usuarioService.login(email, contrasena);
        return u != null;
    }

}
