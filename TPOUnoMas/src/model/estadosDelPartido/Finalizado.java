package model.estadosDelPartido;

import enums.EstadoPartido;
import model.entity.Notificacion;
import model.entity.Partido;
import model.entity.PartidoContext;
import model.entity.Usuario;

import java.util.Date;

public class Finalizado implements IEstadoPartido {


    @Override
    public void agregarJugador(PartidoContext contexto, Usuario jugador) {
        throw new IllegalStateException("No se pueden agregar jugadores a un partido finalizado.");
    }

    @Override
    public void cancelar(PartidoContext contexto) {
        throw new IllegalStateException("No se puede cancelar un partido finalizado.");
    }

    @Override
    public void finalizar(PartidoContext contexto) {
        Partido partido = contexto.getPartido();

        Notificacion n = new Notificacion();
        n.setIdPartido(partido.getId());
        n.setFechaCreacion(new Date());
        n.setMensaje("El partido ha finalizado. ¡Gracias por participar!");
        partido.agregarNotificacion(n);
        partido.notificarObservadores("El partido ha finalizado.");
    }

    @Override
    public String getNombre() {
        return EstadoPartido.FINALIZADO.name();
    }

}