package model.entity;

import model.estadosDelPartido.EstadoPartidoFactory;
import model.estadosDelPartido.IEstadoPartido;

public class PartidoContext {
    private Partido partido;
    private IEstadoPartido estadoActual;

    public PartidoContext(Partido partido) {
        this.partido = partido;
        this.estadoActual = EstadoPartidoFactory.crearEstado(partido.getEstado());
    }

    public void agregarJugador(Usuario jugador) {
        estadoActual.agregarJugador(this, jugador);
    }

    public void cancelar(String motivo) {
        estadoActual.cancelar(this);
    }

    public void finalizar() {
        estadoActual.finalizar(this);
    }

    public Partido getPartido() {
        return partido;
    }

    public boolean permiteCambioDeEstrategia() {
        return estadoActual.permiteCambioDeEstrategia();
    }

    public void setEstadoActual(IEstadoPartido nuevoEstado) {
        this.estadoActual = nuevoEstado;
        this.partido.setEstado(EstadoPartidoFactory.obtenerEnumEstado(nuevoEstado));
    }
}
