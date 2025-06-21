package model.estadosDelPartido;

import model.entity.Partido;
import model.entity.Usuario;

public class Finalizado implements IEstadoPartido {

    @Override
    public boolean manejarNuevoJugador(Partido contexto, Usuario jugador) {
        System.out.println("[Finalizado] No se pueden agregar jugadores");
        return false;
    }

    @Override
    public boolean manejarConfirmacion(Partido contexto, Usuario jugador) {
        System.out.println("[Finalizado] No se pueden confirmar jugadores");
        return false;
    }

    @Override
    public void manejarCancelacion(Partido contexto) {
        System.out.println("[Finalizado] No se puede cancelar un partido finalizado");
    }

    @Override
    public void verificarTransicion(Partido contexto) {
        System.out.println("[Finalizado] Sin transiciones posibles");
    }

    @Override
    public String getNombre() { return "Finalizado"; }

    @Override
    public boolean permiteTransicionA(IEstadoPartido nuevoEstado) {
        return false;
    }

}