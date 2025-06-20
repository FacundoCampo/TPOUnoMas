package model.state;

import model.Partido;
import model.Usuario;

public interface IEstadoPartido {
    boolean manejarNuevoJugador(Partido contexto, Usuario jugador);
    boolean manejarConfirmacion(Partido contexto, Usuario jugador);
    void manejarCancelacion(Partido contexto);
    void verificarTransicion(Partido contexto);
    String getNombre();
    
    default boolean puedeAgregarJugadores() { return false; }
    default boolean puedeCancelar() { return false; }
    default boolean puedeConfirmar() { return false; }
	
}