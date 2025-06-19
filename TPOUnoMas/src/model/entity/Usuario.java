package model.entity;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

import model.staticdb.DataBase;

public class Usuario {
    private String id;
    private String nombre;
    private String email;
    private String contraseña;
    private String ubicacion;
    private List<DeporteUsuario> deportesUsuario;
    private List<Partido> historialPartidos;
    
    public Usuario() {
        this.deportesUsuario = new ArrayList<>();
        this.historialPartidos = new ArrayList<>();
    }
    
    public Usuario(String nombre, String email, String contraseña, String ubicacion) {
        this();
        this.nombre = nombre;
        this.email = email;
        this.contraseña = contraseña;
        this.ubicacion = ubicacion;
    }
    
    public Usuario(String id, String nombre, String email, String contraseña, String ubicacion) {
        this(nombre, email, contraseña, ubicacion);
        this.id = id;
    }
    
    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        this.nombre = nombre.trim();
    }
    
    public String getEmail() { return email; }
    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }
        this.email = email.trim().toLowerCase();
    }
    
    public String getContraseña() { return contraseña; }
    public void setContraseña(String contraseña) {
        if (contraseña == null || contraseña.length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        }
        this.contraseña = contraseña;
    }
    
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    
    public List<DeporteUsuario> getDeportesUsuario() { return new ArrayList<>(deportesUsuario); }
    public void setDeportesUsuario(List<DeporteUsuario> deportesUsuario) {
        this.deportesUsuario = deportesUsuario != null ? new ArrayList<>(deportesUsuario) : new ArrayList<>();
    }
    
    public List<Partido> getHistorialPartidos() { return new ArrayList<>(historialPartidos); }
    public void setHistorialPartidos(List<Partido> historialPartidos) {
        this.historialPartidos = historialPartidos != null ? new ArrayList<>(historialPartidos) : new ArrayList<>();
    }
    
    public void agregarPartidoAHistorial(Partido partido) {
        if (partido != null && !historialPartidos.contains(partido)) {
            historialPartidos.add(partido);
        }
    }
    
    public int getCantidadPartidosJugados() {
        return historialPartidos.size();
    }
    
    public void agregarDeporteUsuario(DeporteUsuario deporteUsuario) {
        if (deporteUsuario == null || !deporteUsuario.esValido()) {
            throw new IllegalArgumentException("El DeporteUsuario no es válido");
        }
        deportesUsuario.add(deporteUsuario);
    }
    
    public NivelJuego getNivelJuegoParaDeporte(Deporte deporte) {
        if (deporte == null) return null;
        
        for (DeporteUsuario deporteUsuario : deportesUsuario) {
            if (deporteUsuario.getDeporte().equals(deporte)) {
                return deporteUsuario.getNivelJuego();
            }
        }
        return null;
    }
    
    public boolean juegarDeporte(Deporte deporte) {
        return getNivelJuegoParaDeporte(deporte) != null;
    }
    
    public boolean esValido() {
        return id != null && !id.trim().isEmpty() &&
               nombre != null && !nombre.trim().isEmpty() &&
               email != null && !email.trim().isEmpty() &&
               contraseña != null && contraseña.length() >= 6;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Usuario usuario = (Usuario) obj;
        return Objects.equals(id, usuario.id) && Objects.equals(email, usuario.email);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
    
    @Override
    public String toString() {
        return "Usuario{id='" + id + "', nombre='" + nombre + "', email='" + email + "'}";
    }

	public void crearUsuario(Usuario nuevo) {
		DataBase.usuarios.add(nuevo);
		
	}
}