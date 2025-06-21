package model.estadosDelPartido;

import model.entity.Partido;
import model.entity.Usuario;
import java.util.Date;

public class Cancelado implements IEstadoPartido {

    private Date fechaCancelacion;
    private String motivoCancelacion;

    public Cancelado() {
        this.fechaCancelacion = new Date();
        this.motivoCancelacion = "No especificado";
        System.out.println("[STATE] Partido cambiado a estado Cancelado");
    }

    public Cancelado(String motivoCancelacion) {
        this.fechaCancelacion = new Date();
        this.motivoCancelacion = motivoCancelacion != null ? motivoCancelacion : "No especificado";
        System.out.println("[STATE] Partido cambiado a estado Cancelado con motivo: " + this.motivoCancelacion);
    }

    @Override
    public boolean manejarNuevoJugador(Partido contexto, Usuario jugador) {
        System.out.println("[Cancelado] No se pueden agregar jugadores");
        return false;
    }

    @Override
    public boolean manejarConfirmacion(Partido contexto, Usuario jugador) {
        System.out.println("[Cancelado] No se pueden confirmar jugadores");
        return false;
    }

    @Override
    public void manejarCancelacion(Partido contexto) {
        System.out.println("[Cancelado] Ya está cancelado desde: " + fechaCancelacion);
    }

    @Override
    public void verificarTransicion(Partido contexto) {
        System.out.println("[Cancelado] Sin transiciones disponibles");
    }

    @Override
    public boolean permiteTransicionA(IEstadoPartido nuevoEstado) {
        return false; // no se permite cambiar a otro estado desde aquí
    }

    @Override
    public String getNombre() { return "Cancelado"; }

    public Date getFechaCancelacion() { return fechaCancelacion; }
    public String getMotivoCancelacion() { return motivoCancelacion; }
}