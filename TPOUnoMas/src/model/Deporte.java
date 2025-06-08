package model;

import java.util.Objects;

public class Deporte {
    private String id;
    private String nombre;
    private int cantidadJugadoresEstandar;
    
    public Deporte() {}
    
    public Deporte(String id, String nombre, int cantidadJugadoresEstandar) {
        this.id = id;
        this.nombre = nombre;
        this.cantidadJugadoresEstandar = cantidadJugadoresEstandar;
    }
    
    public Deporte(String nombre, int cantidadJugadoresEstandar) {
        this.nombre = nombre;
        this.cantidadJugadoresEstandar = cantidadJugadoresEstandar;
    }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del deporte no puede ser null o vacío");
        }
        this.nombre = nombre.trim();
    }
    
    public int getCantidadJugadoresEstandar() { return cantidadJugadoresEstandar; }
    public void setCantidadJugadoresEstandar(int cantidadJugadoresEstandar) {
        if (cantidadJugadoresEstandar <= 0) {
            throw new IllegalArgumentException("La cantidad de jugadores debe ser mayor a cero");
        }
        this.cantidadJugadoresEstandar = cantidadJugadoresEstandar;
    }
    
    public int getTotalJugadoresNecesarios() {
        return cantidadJugadoresEstandar * 2; // Dos equipos
    }
    
    public boolean esValido() {
        return nombre != null && !nombre.trim().isEmpty() && cantidadJugadoresEstandar > 0;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Deporte deporte = (Deporte) obj;
        return Objects.equals(id, deporte.id) && Objects.equals(nombre, deporte.nombre);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, nombre);
    }
    
    @Override
    public String toString() {
        return "Deporte{id='" + id + "', nombre='" + nombre + "', jugadores=" + cantidadJugadoresEstandar + "}";
    }
}