package model.estadosDelPartido;

import model.Partido;
import model.Usuario;
import model.Notificacion;
import java.util.Date;

public class NecesitamosJugadores implements IEstadoPartido {
    
    @Override
    public boolean manejarNuevoJugador(Partido contexto, Usuario jugador) {
        if (contexto == null || jugador == null) return false;
        if (contexto.estaCompleto() || contexto.estaInscrito(jugador)) return false;
        
        boolean agregado = contexto.agregarJugadorDirecto(jugador);
        if (agregado) {
            jugador.agregarPartidoAHistorial(contexto);
            verificarTransicion(contexto);
        }
        return agregado;
    }
    
    @Override
    public boolean manejarConfirmacion(Partido contexto, Usuario jugador) {
        return false; // En este estado no se manejan confirmaciones
    }
    
    @Override
    public void manejarCancelacion(Partido contexto) {
        if (contexto == null) return;
        contexto.setEstado(new Cancelado());
        notificarCancelacion(contexto);
    }
    
    @Override
    public void verificarTransicion(Partido contexto) {
        if (contexto != null && contexto.estaCompleto()) {
            contexto.setEstado(new PartidoArmadoI());
            notificarPartidoArmado(contexto);
        }
    }
    
    @Override
    public String getNombre() { return "Necesitamos jugadores"; }
    
    @Override
    public boolean puedeAgregarJugadores() { return true; }
    
    @Override
    public boolean puedeCancelar() { return true; }
    
    private void notificarCancelacion(Partido contexto) {
        try {
            Notificacion notificacion = new Notificacion();
            notificacion.setIdPartido(contexto.getId());
            notificacion.setMensaje("El partido ha sido cancelado");
            notificacion.setFechaCreacion(new Date());
            contexto.agregarNotificacion(notificacion);
        } catch (Exception e) {
            System.err.println("Error al notificar cancelación: " + e.getMessage());
        }
    }
    
    private void notificarPartidoArmado(Partido contexto) {
        try {
            Notificacion notificacion = new Notificacion();
            notificacion.setIdPartido(contexto.getId());
            notificacion.setMensaje("¡El partido está armado! Se alcanzó el número de jugadores necesarios.");
            notificacion.setFechaCreacion(new Date());
            contexto.agregarNotificacion(notificacion);
        } catch (Exception e) {
            System.err.println("Error al notificar partido armado: " + e.getMessage());
        }
    }
    
    @Override
    public String toString() {
        return "NecesitamosJugadores{nombre='" + getNombre() + "'}";
    }
}