package service;

import model.Usuario;
import repository.UsuarioRepository;

public class UsuarioService {
    private UsuarioRepository usuarioRepository;
    
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    
    public Usuario registrarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser null");
        }
        
        if (usuario.getId() == null || usuario.getId().trim().isEmpty()) {
            usuario.setId("USR_" + System.currentTimeMillis());
        }
        
        return usuarioRepository.guardar(usuario);
    }
    
    public Usuario buscarUsuario(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID no puede ser null o vacío");
        }
        return usuarioRepository.buscarPorId(id);
    }
    
    public Usuario actualizarPerfil(Usuario usuario) {
        if (usuario == null || usuario.getId() == null) {
            throw new IllegalArgumentException("Usuario inválido para actualizar");
        }
        return usuarioRepository.actualizar(usuario);
    }
    
    public UsuarioRepository getUsuarioRepository() { return usuarioRepository; }
}