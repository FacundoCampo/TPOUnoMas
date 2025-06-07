package repository;

import model.Usuario;
import model.Deporte;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Repositorio para gestionar la persistencia de usuarios
 * Implementa operaciones CRUD para la entidad Usuario
 */
public class UsuarioRepository {
    
    // Simulamos una base de datos en memoria usando un Map
    private Map<String, Usuario> usuarios;
    
    /**
     * Constructor del repositorio de usuarios
     */
    public UsuarioRepository() {
        this.usuarios = new ConcurrentHashMap<>();
    }
    
    /**
     * Guarda un usuario en el repositorio
     * @param usuario el usuario a guardar
     * @return el usuario guardado
     * @throws IllegalArgumentException si el usuario es null o inválido
     */
    public Usuario guardar(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser null");
        }
        
        if (usuario.getId() == null || usuario.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("El usuario debe tener un ID válido");
        }
        
        try {
            // Clonar el usuario para evitar modificaciones externas
            Usuario usuarioClonado = clonarUsuario(usuario);
            usuarios.put(usuario.getId(), usuarioClonado);
            return usuarioClonado;
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar usuario: " + e.getMessage(), e);
        }
    }
    
    /**
     * Busca un usuario por su ID
     * @param id el ID del usuario a buscar
     * @return el usuario encontrado o null si no existe
     * @throws IllegalArgumentException si el ID es null o vacío
     */
    public Usuario buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID no puede ser null o vacío");
        }
        
        Usuario usuario = usuarios.get(id);
        return usuario != null ? clonarUsuario(usuario) : null;
    }
    
    /**
     * Busca usuarios por su deporte favorito
     * @param deporte el deporte favorito a buscar
     * @return lista de usuarios que tienen ese deporte como favorito
     * @throws IllegalArgumentException si el deporte es null
     */
    public List<Usuario> buscarPorDeporteFavorito(Deporte deporte) {
        if (deporte == null) {
            throw new IllegalArgumentException("El deporte no puede ser null");
        }
        
        List<Usuario> usuariosEncontrados = new ArrayList<>();
        
        for (Usuario usuario : usuarios.values()) {
            if (usuario.getDeportesUsuario() != null) {
                boolean tieneDeporteFavorito = usuario.getDeportesUsuario().stream()
                    .anyMatch(deporteUsuario -> 
                        deporteUsuario.isFavorito() && 
                        deporteUsuario.getDeporte() != null &&
                        deporteUsuario.getDeporte().getId().equals(deporte.getId())
                    );
                
                if (tieneDeporteFavorito) {
                    usuariosEncontrados.add(clonarUsuario(usuario));
                }
            }
        }
        
        return usuariosEncontrados;
    }
    
    /**
     * Busca un usuario por su email
     * @param email el email del usuario a buscar
     * @return el usuario encontrado o null si no existe
     * @throws IllegalArgumentException si el email es null o vacío
     */
    public Usuario buscarPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede ser null o vacío");
        }
        
        for (Usuario usuario : usuarios.values()) {
            if (email.equalsIgnoreCase(usuario.getEmail())) {
                return clonarUsuario(usuario);
            }
        }
        
        return null;
    }
    
    /**
     * Actualiza un usuario existente
     * @param usuario el usuario con los datos actualizados
     * @return el usuario actualizado
     * @throws IllegalArgumentException si el usuario es null o no tiene ID
     * @throws RuntimeException si el usuario no existe
     */
    public Usuario actualizar(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser null");
        }
        
        if (usuario.getId() == null || usuario.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("El usuario debe tener un ID válido para actualizar");
        }
        
        if (!usuarios.containsKey(usuario.getId())) {
            throw new RuntimeException("No se encontró usuario con ID: " + usuario.getId());
        }
        
        try {
            Usuario usuarioClonado = clonarUsuario(usuario);
            usuarios.put(usuario.getId(), usuarioClonado);
            return usuarioClonado;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar usuario: " + e.getMessage(), e);
        }
    }
    
    /**
     * Elimina un usuario por su ID
     * @param id el ID del usuario a eliminar
     * @return true si se eliminó exitosamente, false si no existía
     * @throws IllegalArgumentException si el ID es null o vacío
     */
    public boolean eliminar(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID no puede ser null o vacío");
        }
        
        return usuarios.remove(id) != null;
    }
    
    /**
     * Obtiene todos los usuarios
     * @return lista con todos los usuarios registrados
     */
    public List<Usuario> buscarTodos() {
        List<Usuario> todosLosUsuarios = new ArrayList<>();
        
        for (Usuario usuario : usuarios.values()) {
            todosLosUsuarios.add(clonarUsuario(usuario));
        }
        
        return todosLosUsuarios;
    }
    
    /**
     * Busca usuarios por ubicación (búsqueda aproximada)
     * @param ubicacion la ubicación a buscar
     * @return lista de usuarios en esa ubicación
     * @throws IllegalArgumentException si la ubicación es null o vacía
     */
    public List<Usuario> buscarPorUbicacion(String ubicacion) {
        if (ubicacion == null || ubicacion.trim().isEmpty()) {
            throw new IllegalArgumentException("La ubicación no puede ser null o vacía");
        }
        
        List<Usuario> usuariosEncontrados = new ArrayList<>();
        String ubicacionBusqueda = ubicacion.toLowerCase().trim();
        
        for (Usuario usuario : usuarios.values()) {
            if (usuario.getUbicacion() != null && 
                usuario.getUbicacion().toLowerCase().contains(ubicacionBusqueda)) {
                usuariosEncontrados.add(clonarUsuario(usuario));
            }
        }
        
        return usuariosEncontrados;
    }
    
    /**
     * Obtiene la cantidad total de usuarios registrados
     * @return el número total de usuarios
     */
    public int contarUsuarios() {
        return usuarios.size();
    }
    
    /**
     * Verifica si existe un usuario con el ID especificado
     * @param id el ID a verificar
     * @return true si existe, false en caso contrario
     */
    public boolean existeUsuario(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        return usuarios.containsKey(id);
    }
    
    /**
     * Limpia todos los usuarios del repositorio (útil para testing)
     */
    public void limpiar() {
        usuarios.clear();
    }
    
    /**
     * Clona un usuario para evitar modificaciones externas
     * @param usuario el usuario original
     * @return una copia del usuario
     */
    private Usuario clonarUsuario(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        
        // Creamos una nueva instancia de Usuario con los mismos datos
        Usuario clon = new Usuario();
        clon.setId(usuario.getId());
        clon.setNombre(usuario.getNombre());
        clon.setEmail(usuario.getEmail());
        clon.setContraseña(usuario.getContraseña());
        clon.setUbicacion(usuario.getUbicacion());
        
        // Copiar deportes de usuario
        if (usuario.getDeportesUsuario() != null) {
            clon.setDeportesUsuario(new ArrayList<>(usuario.getDeportesUsuario()));
        }
        
        // Copiar historial de partidos
        if (usuario.getHistorialPartidos() != null) {
            clon.setHistorialPartidos(new ArrayList<>(usuario.getHistorialPartidos()));
        }
        
        return clon;
    }
}