package model.dto;

import java.util.List;
import java.util.ArrayList;

public class UsuarioDTO {
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

    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getContrasena() { return contrasena; }
    public String getUbicacion() { return ubicacion; }

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