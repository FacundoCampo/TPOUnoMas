package controller;

import model.dto.UsuarioDTO;
import model.entity.Usuario;
import model.service.UsuarioService;

public class UsuarioController {
    private UsuarioService usuarioService;
    private Usuario usuario;
    
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    
    public UsuarioController() {
    	usuario = new Usuario();
    }
    
    public Usuario registrarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser null");
        }
        
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es requerido");
        }
        
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es requerido");
        }
        
        return usuarioService.registrarUsuario(usuario);
    }
    
    public Usuario buscarUsuario(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID no puede ser null o vacío");
        }
        
        Usuario usuario = usuarioService.buscarUsuario(id);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado: " + id);
        }
        return usuario;
    }
    
    public Usuario actualizarPerfil(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser null");
        }
        
        if (usuario.getId() == null || usuario.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("El ID es requerido para actualizar");
        }
        
        return usuarioService.actualizarPerfil(usuario);
    }
    
    public UsuarioService getUsuarioService() { return usuarioService; }

	public void crearUsuario(UsuarioDTO usuarioDTO) {
		Usuario nuevo = new Usuario();
		nuevo.setId(usuarioDTO.getId());
		
		usuario.crearUsuario(nuevo);
		
	}
}