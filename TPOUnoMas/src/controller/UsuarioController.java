package controller;

import model.Usuario;
import service.UsuarioService;

/**
 * Controlador para gestionar las operaciones relacionadas con usuarios
 * Implementa el patrón MVC como capa de control
 */
public class UsuarioController {
    
    private UsuarioService usuarioService;
    
    /**
     * Constructor del controlador de usuarios
     * @param usuarioService servicio para operaciones de usuario
     */
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    
    /**
     * Registra un nuevo usuario en el sistema
     * @param usuario el usuario a registrar
     * @return el usuario registrado con su ID asignado
     * @throws IllegalArgumentException si el usuario es null o datos inválidos
     */
    public Usuario registrarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser null");
        }
        
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del usuario es requerido");
        }
        
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email del usuario es requerido");
        }
        
        try {
            return usuarioService.registrarUsuario(usuario);
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar usuario: " + e.getMessage(), e);
        }
    }
    
    /**
     * Busca un usuario por su ID
     * @param id el ID del usuario a buscar
     * @return el usuario encontrado
     * @throws IllegalArgumentException si el ID es null o vacío
     * @throws RuntimeException si no se encuentra el usuario
     */
    public Usuario buscarUsuario(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del usuario no puede ser null o vacío");
        }
        
        try {
            Usuario usuario = usuarioService.buscarUsuario(id);
            if (usuario == null) {
                throw new RuntimeException("Usuario no encontrado con ID: " + id);
            }
            return usuario;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar usuario: " + e.getMessage(), e);
        }
    }
    
    /**
     * Actualiza el perfil de un usuario existente
     * @param usuario el usuario con los datos actualizados
     * @return el usuario actualizado
     * @throws IllegalArgumentException si el usuario es null o el ID es inválido
     */
    public Usuario actualizarPerfil(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser null");
        }
        
        if (usuario.getId() == null || usuario.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del usuario es requerido para actualizar");
        }
        
        try {
            return usuarioService.actualizarPerfil(usuario);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar perfil: " + e.getMessage(), e);
        }
    }
    
    /**
     * Obtiene el servicio de usuario (para testing o casos especiales)
     * @return el servicio de usuario
     */
    public UsuarioService getUsuarioService() {
        return usuarioService;
    }
    
    /**
     * Establece el servicio de usuario
     * @param usuarioService el nuevo servicio de usuario
     */
    public void setUsuarioService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
}