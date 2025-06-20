package model.state;

import model.Partido;
import model.Usuario;

public class Finalizado implements IEstadoPartido {
    
    @Override
    public boolean manejarNuevoJugador(Partido contexto, Usuario jugador) {
        return false; // No se pueden agregar jugadores a un partido finalizado
    }
    
    @Override
    public boolean manejarConfirmacion(Partido contexto, Usuario jugador) {
        return false; // No se pueden confirmar jugadores en un partido finalizado
    }
    
    @Override
    public void manejarCancelacion(Partido contexto) {
        System.out.println("No se puede cancelar un partido que ya ha finalizado");
    }
    
    @Override
    public void verificarTransicion(Partido contexto) {
        System.out.println("Partido finalizado - no hay transiciones de estado disponibles");
    }
    
    @Override
    public String getNombre() { return "Finalizado"; }
}