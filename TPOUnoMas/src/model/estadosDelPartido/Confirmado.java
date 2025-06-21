package model.estadosDelPartido;

import model.entity.Partido;
import model.entity.Usuario;

public class Confirmado implements IEstadoPartido {

    @Override
    public boolean manejarNuevoJugador(Partido contexto, Usuario jugador) {
        System.out.println("[Confirmado] No se pueden agregar jugadores");
        return false;
    }

    @Override
    public boolean manejarConfirmacion(Partido contexto, Usuario jugador) {
        System.out.println("[Confirmado] Jugadores ya confirmados");
        return false;
    }

    @Override
    public void manejarCancelacion(Partido contexto) {
        System.out.println("[Confirmado] Partido cancelado");
        if (contexto != null) {
            contexto.setEstado(new Cancelado());
        }
    }

    @Override
    public void verificarTransicion(Partido contexto) {
        System.out.println("[Confirmado] Verificando transición...");
        if (contexto == null) return;
        long horasHastaPartido = contexto.getHorasHastaPartido();
        if (horasHastaPartido <= 0) {
            contexto.setEstado(new EnJuego());
        } else if (!contexto.estaCompleto()) {
            contexto.setEstado(new NecesitamosJugadores());
        }
    }

    @Override
    public String getNombre() { return "Confirmado"; }
    @Override
    public boolean puedeCancelar() { return true; }
}
