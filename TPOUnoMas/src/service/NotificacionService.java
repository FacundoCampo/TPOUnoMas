package service;

import model.Partido;
import model.Usuario;
import model.Notificacion;
import observer.StrategyNotificacion;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

public class NotificacionService {
    private List<StrategyNotificacion> observadores;
    
    public NotificacionService() {
        this.observadores = new ArrayList<>();
    }
    
    public void agregarObservador(StrategyNotificacion observador) {
        if (observador != null && !observadores.contains(observador)) {
            observadores.add(observador);
        }
    }
    
    public void eliminarObservador(StrategyNotificacion observador) {
        observadores.remove(observador);
    }
    
    public void notificarCambioEstado(Partido partido, String estadoAnterior) {
        if (partido == null) return;
        
        try {
            for (Usuario jugador : partido.getJugadoresInscritos()) {
                Notificacion notificacion = new Notificacion(
                    partido.getId(), jugador.getId(),
                    "El partido cambió de '" + estadoAnterior + "' a '" + partido.getEstado().getNombre() + "'"
                );
                partido.agregarNotificacion(notificacion);
            }
            
            for (StrategyNotificacion observador : observadores) {
                observador.notificarCambioEstado(partido, estadoAnterior);
            }
        } catch (Exception e) {
            System.err.println("Error al notificar cambio de estado: " + e.getMessage());
        }
    }
    
    public void notificarNuevoPartido(Partido partido) {
        if (partido == null) return;
        
        try {
            // En un sistema real, aquí se obtendrían usuarios interesados
            List<Usuario> usuariosInteresados = new ArrayList<>();
            
            for (StrategyNotificacion observador : observadores) {
                observador.notificarNuevoPartido(partido, usuariosInteresados);
            }
        } catch (Exception e) {
            System.err.println("Error al notificar nuevo partido: " + e.getMessage());
        }
    }
    
    public int getCantidadObservadores() { return observadores.size(); }
    public List<StrategyNotificacion> getObservadores() { return new ArrayList<>(observadores); }
}