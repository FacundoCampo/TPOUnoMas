package model.dto;

import enums.NivelJuego;
import model.DeporteUsuario;
import model.Partido;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class UsuarioDTO {
    private String id;
    private String nombre;
    private String email;
    private String contrasena;
    private String ubicacion;
    private List<DeporteUsuarioDTO> preferenciasDeportivas;

    public UsuarioDTO(String nombre, String email, String contrasena, String ubicacion) {
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.ubicacion = ubicacion;
        this.preferenciasDeportivas = new ArrayList<>();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getContrasena() { return contrasena; }
    public String getUbicacion() { return ubicacion; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEmail(String email) { this.email = email; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public List<DeporteUsuarioDTO> getPreferenciasDeportivas() {
        return preferenciasDeportivas;
    }

    public void setPreferenciasDeportivas(List<DeporteUsuarioDTO> lista) {
        this.preferenciasDeportivas = new ArrayList<>();
        if (lista != null) {
            for (DeporteUsuarioDTO item : lista) {
                this.preferenciasDeportivas.add(item);
            }
        }
    }

}