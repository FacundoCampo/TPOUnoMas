package model.estadosDelPartido;

import enums.EstadoPartido;

public class EstadoPartidoFactory {
    public static IEstadoPartido crearEstado(EstadoPartido estado) {
        return switch (estado) {
            case ENJUEGO -> new EnJuego();
            case FINALIZADO -> new Finalizado();
            case CANCELADO -> new Cancelado();
            case CONFIRMADO -> new Confirmado();
            case NECESITAMOSJUGADORES -> new NecesitamosJugadores();
            case PARTIDOARMADOI -> new PartidoArmadoI();
        };
    }

    public static EstadoPartido obtenerEnumEstado(IEstadoPartido estado) {
        if (estado instanceof EnJuego) return EstadoPartido.ENJUEGO;
        if (estado instanceof Finalizado) return EstadoPartido.FINALIZADO;
        if (estado instanceof Cancelado) return EstadoPartido.CANCELADO;
        if (estado instanceof Confirmado) return EstadoPartido.CONFIRMADO;
        if (estado instanceof NecesitamosJugadores) return EstadoPartido.NECESITAMOSJUGADORES;
        if (estado instanceof PartidoArmadoI) return EstadoPartido.PARTIDOARMADOI;
        throw new IllegalArgumentException("Estado desconocido");
    }
}
