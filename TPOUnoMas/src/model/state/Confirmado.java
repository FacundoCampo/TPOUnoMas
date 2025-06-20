package model.state;

import model.Partido;
import model.Usuario;

public class Confirmado implements IEstadoPartido {
    
    @Override
    public boolean manejarNuevoJugador(Partido contexto, Usuario jugador) {
        return false; // No se pueden agregar más jugadores
    }
    
    @Override
    public boolean manejarConfirmacion(Partido contexto, Usuario jugador) {
        return false; // Ya todos confirmaron
    }
    
    @Override
    public void manejarCancelacion(Partido contexto) {
        if (contexto != null) {
            contexto.setEstado(new Cancelado());
        }
    }
    
    @Override
    public void verificarTransicion(Partido contexto) {
        if (contexto == null) return;
        
        long horasHastaPartido = contexto.getHorasHastaPartido();
        if (horasHastaPartido <= 0) {
            contexto.setEstado(new EnJuego());
        }
        
        if (!contexto.estaCompleto()) {
            contexto.setEstado(new NecesitamosJugadores());
        }
    }
    
    @Override
    public String getNombre() { return "Confirmado"; }
    
    @Override
    public boolean puedeCancelar() { return true; }
}