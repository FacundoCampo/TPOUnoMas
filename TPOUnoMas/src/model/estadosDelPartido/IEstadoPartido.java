package model.estadosDelPartido;

import model.entity.Partido;
import model.entity.Usuario;

public interface IEstadoPartido {
    boolean manejarNuevoJugador(Partido contexto, Usuario jugador);
    boolean manejarConfirmacion(Partido contexto, Usuario jugador);
    void manejarCancelacion(Partido contexto);
    void verificarTransicion(Partido contexto);
    String getNombre();
    
    default boolean puedeAgregarJugadores() { return false; }
    default boolean puedeCancelar() { return false; }
    default boolean puedeConfirmar() { return false; }
    default boolean permiteTransicionA(IEstadoPartido nuevoEstado) {
        return true;
    }
    default boolean permiteCambioDeEstrategia() {
        return false;
    }
	
}