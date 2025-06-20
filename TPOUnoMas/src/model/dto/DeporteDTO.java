package model.dto;

import java.util.UUID;

public class DeporteDTO {
    private String id;
    private String nombre;
    private int cantidadJugadoresEstandar;

    public DeporteDTO(String id, String nombre, int cantidadJugadoresEstandar) {
        this.id = id == null ? UUID.randomUUID().toString() : id;
        this.nombre = nombre;
        this.cantidadJugadoresEstandar = cantidadJugadoresEstandar;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public int getCantidadJugadoresEstandar() { return cantidadJugadoresEstandar; }

    public void setId(String id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCantidadJugadoresEstandar(int cantidad) { this.cantidadJugadoresEstandar = cantidad; }
}
