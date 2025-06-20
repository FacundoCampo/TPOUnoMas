package strategy;

import enums.TipoEmparejamiento;

public class EmparejamientoStrategyFactory {
    public static IEstrategiaEmparejamiento crear(TipoEmparejamiento tipo) {
        return switch (tipo) {
            case CERCANIA -> new EmparejamientoPorCercania();
            case HISTORIAL -> new EmparejamientoPorHistorial();
            case NIVEL -> new EmparejamientoPorNivel();
        };
    }
}
