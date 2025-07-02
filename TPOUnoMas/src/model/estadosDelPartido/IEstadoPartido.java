package model.estadosDelPartido;

import model.entity.PartidoContext;
import model.entity.Usuario;

public interface IEstadoPartido {
    void agregarJugador(PartidoContext contexto, Usuario jugador);
    void cancelar(PartidoContext contexto);
    void finalizar(PartidoContext contexto);
    String getNombre();
    default boolean permiteCambioDeEstrategia() { return false; }
	
}