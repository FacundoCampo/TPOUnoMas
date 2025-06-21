package model.entity;

import enums.NivelJuego;
import model.entity.interfaces.IObserver;

import java.util.List;
import java.util.ArrayList;

public class Usuario implements IObserver {
    private String nombre;
    private String email; //es unico y se utiliza como ID
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
    }
    
    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre.trim(); }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email.trim().toLowerCase(); }
    
    public String getContraseña() { return contraseña; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }
    
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

    public NivelJuego getNivelJuegoParaDeporte(Deporte deporte) {
        if (deporte == null || deporte.getNombre() == null) return null;

        for (DeporteUsuario deporteUsuario : deportesUsuario) {
            Deporte d = deporteUsuario.getDeporte();
            if (d != null && d.getNombre() != null &&
                    d.getNombre().equalsIgnoreCase(deporte.getNombre())) {
                return deporteUsuario.getNivelJuego();
            }
        }
        return null;
    }
    
    public boolean juegarDeporte(Deporte deporte) {
        return getNivelJuegoParaDeporte(deporte) != null;
    }

    @Override
    public void actualizar(String mensaje) {
        System.out.println("[Notificación para " + this.nombre + "]: " + mensaje);
    }

    @Override
    public String toString() {
        return "Usuario{nombre='" + nombre + "', email='" + email + "'}";
    }
}