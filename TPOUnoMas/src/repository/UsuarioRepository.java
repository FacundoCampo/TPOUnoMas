package repository;

import model.Usuario;
import model.Deporte;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class UsuarioRepository {
    private Map<String, Usuario> usuarios;
    
    public UsuarioRepository() {
        this.usuarios = new ConcurrentHashMap<>();
    }
    
    public Usuario guardar(Usuario usuario) {
        if (usuario == null || usuario.getId() == null) {
            throw new IllegalArgumentException("Usuario o ID inválido");
        }
        usuarios.put(usuario.getId(), clonarUsuario(usuario));
        return clonarUsuario(usuario);
    }
    
    public Usuario buscarPorId(String id) {
        if (id == null) return null;
        Usuario usuario = usuarios.get(id);
        return usuario != null ? clonarUsuario(usuario) : null;
    }
    
    public Usuario buscarPorEmail(String email) {
        if (email == null) return null;
        for (Usuario usuario : usuarios.values()) {
            if (email.equalsIgnoreCase(usuario.getEmail())) {
                return clonarUsuario(usuario);
            }
        }
        return null;
    }
    
    public List<Usuario> buscarPorDeporteFavorito(Deporte deporte) {
        if (deporte == null) return new ArrayList<>();
        
        List<Usuario> resultado = new ArrayList<>();
        for (Usuario usuario : usuarios.values()) {
            boolean tieneDeporteFavorito = usuario.getDeportesUsuario().stream()
                .anyMatch(du -> du.isFavorito() && 
                               du.getDeporte() != null && 
                               du.getDeporte().getId().equals(deporte.getId()));
            
            if (tieneDeporteFavorito) {
                resultado.add(clonarUsuario(usuario));
            }
        }
        return resultado;
    }
    
    public Usuario actualizar(Usuario usuario) {
        if (usuario == null || usuario.getId() == null) {
            throw new IllegalArgumentException("Usuario inválido");
        }
        if (!usuarios.containsKey(usuario.getId())) {
            throw new RuntimeException("Usuario no encontrado: " + usuario.getId());
        }
        usuarios.put(usuario.getId(), clonarUsuario(usuario));
        return clonarUsuario(usuario);
    }
    
    public boolean eliminar(String id) {
        return id != null && usuarios.remove(id) != null;
    }
    
    public List<Usuario> buscarTodos() {
        List<Usuario> resultado = new ArrayList<>();
        for (Usuario usuario : usuarios.values()) {
            resultado.add(clonarUsuario(usuario));
        }
        return resultado;
    }
    
    public int contarUsuarios() {
        return usuarios.size();
    }
    
    public boolean existeUsuario(String id) {
        return id != null && usuarios.containsKey(id);
    }
    
    public void limpiar() {
        usuarios.clear();
    }
    
    private Usuario clonarUsuario(Usuario usuario) {
        if (usuario == null) return null;
        
        Usuario clon = new Usuario();
        clon.setId(usuario.getId());
        clon.setNombre(usuario.getNombre());
        clon.setEmail(usuario.getEmail());
        clon.setContraseña(usuario.getContraseña());
        clon.setUbicacion(usuario.getUbicacion());
        
        if (usuario.getDeportesUsuario() != null) {
            clon.setDeportesUsuario(new ArrayList<>(usuario.getDeportesUsuario()));
        }
        
        if (usuario.getHistorialPartidos() != null) {
            clon.setHistorialPartidos(new ArrayList<>(usuario.getHistorialPartidos()));
        }
        
        return clon;
    }
}