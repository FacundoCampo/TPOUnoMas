package state;

import model.Partido;
import model.Usuario;
import java.util.Date;

public class Cancelado implements EstadoPartido {
    
    private Date fechaCancelacion;
    private String motivoCancelacion;
    
    public Cancelado() {
        this.fechaCancelacion = new Date();
        this.motivoCancelacion = "No especificado";
    }
    
    public Cancelado(String motivoCancelacion) {
        this.fechaCancelacion = new Date();
        this.motivoCancelacion = motivoCancelacion != null ? motivoCancelacion : "No especificado";
    }
    
    @Override
    public boolean manejarNuevoJugador(Partido contexto, Usuario jugador) {
        return false; // No se pueden agregar jugadores a un partido cancelado
    }
    
    @Override
    public boolean manejarConfirmacion(Partido contexto, Usuario jugador) {
        return false; // No se pueden confirmar jugadores en un partido cancelado
    }
    
    @Override
    public void manejarCancelacion(Partido contexto) {
        System.out.println("El partido ya se encuentra cancelado desde: " + fechaCancelacion);
    }
    
    @Override
    public void verificarTransicion(Partido contexto) {
        System.out.println("Partido cancelado - no hay transiciones de estado disponibles");
    }
    
    @Override
    public String getNombre() { return "Cancelado"; }
    
    public Date getFechaCancelacion() { return fechaCancelacion; }
    public String getMotivoCancelacion() { return motivoCancelacion; }
}