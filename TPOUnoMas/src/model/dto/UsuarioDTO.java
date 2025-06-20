package model.dto;

import model.DeporteUsuario;
import model.Partido;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

public class UsuarioDTO {
    private String id;
    private String nombre;
    private String email;
    private String contraseña;
    private String ubicacion;
    private List<DeporteUsuario> deportesUsuario;
    private List<Partido> historialPartidos;
    
    public UsuarioDTO(String nombre, String email, String contraseña, String ubicacion) {
        super();
        this.nombre = nombre;
        this.email = email;
        this.contraseña = contraseña;
        this.ubicacion = ubicacion;
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

}