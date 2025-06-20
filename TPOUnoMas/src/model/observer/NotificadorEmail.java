package model.observer;

import model.Partido;
import model.Usuario;
import model.adapter.EmailAdapter;
import java.util.List;

public class NotificadorEmail implements StrategyNotificacion {
    private EmailAdapter emailAdapter;
    
    public NotificadorEmail(EmailAdapter emailAdapter) {
        this.emailAdapter = emailAdapter;
    }
    
    @Override
    public void notificarNuevoPartido(Partido partido, List<Usuario> usuarios) {
        if (partido == null || usuarios == null) return;
        
        String asunto = "¡Nuevo partido de " + partido.getDeporte().getNombre() + " disponible!";
        String contenido = "Se creó un nuevo partido en " + partido.getUbicacion() + 
                          " para el " + partido.getFechaHora();
        
        for (Usuario usuario : usuarios) {
            if (usuario.getEmail() != null) {
                try {
                    emailAdapter.enviarEmail(usuario.getEmail(), asunto, contenido);
                } catch (Exception e) {
                    System.err.println("Error enviando email a " + usuario.getEmail());
                }
            }
        }
    }
    
    @Override
    public void notificarCambioEstado(Partido partido, String estadoAnterior) {
        if (partido == null) return;
        
        String asunto = "Actualización de partido - " + partido.getEstado().getNombre();
        String contenido = "El partido cambió de '" + estadoAnterior + "' a '" + 
                          partido.getEstado().getNombre() + "'";
        
        for (Usuario jugador : partido.getJugadoresInscritos()) {
            if (jugador.getEmail() != null) {
                try {
                    emailAdapter.enviarEmail(jugador.getEmail(), asunto, contenido);
                } catch (Exception e) {
                    System.err.println("Error enviando email a " + jugador.getEmail());
                }
            }
        }
    }
}