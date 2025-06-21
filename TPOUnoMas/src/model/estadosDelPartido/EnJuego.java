package model.estadosDelPartido;

import model.entity.Partido;
import model.entity.Usuario;

public class EnJuego implements IEstadoPartido {

    @Override
    public boolean manejarNuevoJugador(Partido contexto, Usuario jugador) {
        System.out.println("[EnJuego] No se pueden agregar jugadores");
        return false;
    }

    @Override
    public boolean manejarConfirmacion(Partido contexto, Usuario jugador) {
        System.out.println("[EnJuego] No se pueden confirmar jugadores");
        return false;
    }

    @Override
    public void manejarCancelacion(Partido contexto) {
        System.out.println("[EnJuego] No se puede cancelar un partido en juego");
    }

    @Override
    public void verificarTransicion(Partido contexto) {
        System.out.println("[EnJuego] Verificando si debe finalizarse");
        if (debeFinalizarse(contexto)) {
            contexto.setEstado(new Finalizado());
            for (Usuario jugador : contexto.getJugadoresInscritos()) {
                jugador.agregarPartidoAHistorial(contexto);
            }
        }
    }

    @Override
    public String getNombre() { return "En juego"; }

    @Override
    public boolean permiteTransicionA(IEstadoPartido nuevoEstado) {
        return false;
    }

    private boolean debeFinalizarse(Partido contexto) {
        if (contexto.getFechaHora() == null) return false;
        long tiempoActual = System.currentTimeMillis();
        long tiempoInicio = contexto.getFechaHora().getTime();
        long duracionMs = contexto.getDuracion() * 60 * 1000L;
        long tiempoFin = tiempoInicio + duracionMs;
        return tiempoActual >= tiempoFin;
    }
}
