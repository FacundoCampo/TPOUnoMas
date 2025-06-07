package service;

import model.Usuario;
import repository.UsuarioRepository;

/**
 * Servicio para gestionar la lógica de negocio relacionada con usuarios
 * Actúa como intermediario entre los controladores y el repositorio
 */
public class UsuarioService {
    
    private UsuarioRepository usuarioRepository;
    
    /**
     * Constructor del servicio de usuario
     * @param usuarioRepository repositorio para operaciones de persistencia
     */
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    
    /**
     * Registra un nuevo usuario en el sistema
     * @param usuario el usuario a registrar
     * @return el usuario registrado con su ID asignado
     * @throws IllegalArgumentException si el usuario es inválido
     * @throws RuntimeException si el email ya existe
     */
    public Usuario registrarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser null");
        }
        
        // Validar datos obligatorios
        validarDatosUsuario(usuario);
        
        // Verificar si el email ya existe
        if (existeUsuarioConEmail(usuario.getEmail())) {
            throw new RuntimeException("Ya existe un usuario registrado con el email: " + usuario.getEmail());
        }
        
        // Generar ID único si no tiene
        if (usuario.getId() == null || usuario.getId().trim().isEmpty()) {
            usuario.setId(generarIdUnico());
        }
        
        try {
            return usuarioRepository.guardar(usuario);
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar usuario en el repositorio: " + e.getMessage(), e);
        }
    }
    
    /**
     * Busca un usuario por su ID
     * @param id el ID del usuario a buscar
     * @return el usuario encontrado o null si no existe
     * @throws IllegalArgumentException si el ID es inválido
     */
    public Usuario buscarUsuario(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del usuario no puede ser null o vacío");
        }
        
        try {
            return usuarioRepository.buscarPorId(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar usuario: " + e.getMessage(), e);
        }
    }
    
    /**
     * Actualiza el perfil de un usuario existente
     * @param usuario el usuario con los datos actualizados
     * @return el usuario actualizado
     * @throws IllegalArgumentException si el usuario es inválido
     * @throws RuntimeException si el usuario no existe
     */
    public Usuario actualizarPerfil(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser null");
        }
        
        if (usuario.getId() == null || usuario.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del usuario es requerido para actualizar");
        }
        
        // Verificar que el usuario existe
        Usuario usuarioExistente = buscarUsuario(usuario.getId());
        if (usuarioExistente == null) {
            throw new RuntimeException("No se encontró usuario con ID: " + usuario.getId());
        }
        
        // Validar datos del usuario
        validarDatosUsuario(usuario);
        
        // Verificar email duplicado (si cambió el email)
        if (!usuarioExistente.getEmail().equals(usuario.getEmail()) && 
            existeUsuarioConEmail(usuario.getEmail())) {
            throw new RuntimeException("Ya existe otro usuario con el email: " + usuario.getEmail());
        }
        
        try {
            return usuarioRepository.actualizar(usuario);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar usuario: " + e.getMessage(), e);
        }
    }
    
    /**
     * Valida los datos obligatorios del usuario
     * @param usuario el usuario a validar
     * @throws IllegalArgumentException si algún dato es inválido
     */
    private void validarDatosUsuario(Usuario usuario) {
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del usuario es requerido");
        }
        
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email del usuario es requerido");
        }
        
        if (!esEmailValido(usuario.getEmail())) {
            throw new IllegalArgumentException("El formato del email es inválido");
        }
        
        if (usuario.getContraseña() == null || usuario.getContraseña().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña del usuario es requerida");
        }
        
        if (usuario.getContraseña().length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        }
    }
    
    /**
     * Verifica si existe un usuario con el email especificado
     * @param email el email a verificar
     * @return true si existe, false en caso contrario
     */
    private boolean existeUsuarioConEmail(String email) {
        try {
            // Esta es una búsqueda simplificada. En un caso real, 
            // el repositorio tendría un método específico para esto
            return usuarioRepository.buscarPorEmail(email) != null;
        } catch (Exception e) {
            // Si hay error en la búsqueda, asumimos que no existe
            return false;
        }
    }
    
    /**
     * Valida el formato del email usando una expresión regular básica
     * @param email el email a validar
     * @return true si el formato es válido, false en caso contrario
     */
    private boolean esEmailValido(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        // Expresión regular básica para validar email
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }
    
    /**
     * Genera un ID único para el usuario
     * En un sistema real, esto podría ser un UUID o generado por la base de datos
     * @return un ID único
     */
    private String generarIdUnico() {
        return "USR_" + System.currentTimeMillis() + "_" + Math.random();
    }
    
    /**
     * Obtiene el repositorio de usuario
     * @return el repositorio de usuario
     */
    public UsuarioRepository getUsuarioRepository() {
        return usuarioRepository;
    }
    
    /**
     * Establece el repositorio de usuario
     * @param usuarioRepository el nuevo repositorio
     */
    public void setUsuarioRepository(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
}