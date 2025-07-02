package services.notificacionService;

import model.entity.Partido;
import model.entity.Usuario;
import services.notificacionService.interfaces.IStrategyNotificacion;

import java.util.List;

public class NotificadorPush implements IStrategyNotificacion {
    private String apiKey;
    private boolean configurado;
    
    public NotificadorPush() {
        this.configurado = false;
    }
    
    public NotificadorPush(String apiKey) {
        this.apiKey = apiKey;
        this.configurado = true;
    }
    
    @Override
    public void notificarNuevoPartido(Partido partido, List<Usuario> usuarios) {
        if (!configurado || partido == null || usuarios == null) return;
        
        System.out.println("=== NOTIFICACIÓN PUSH ===");
        System.out.println("Nuevo partido: " + partido.getDeporte().getNombre());
        System.out.println("Enviando a " + usuarios.size() + " usuarios");
        
        for (Usuario usuario : usuarios) {
            System.out.println("Push enviado a: " + usuario.getNombre());
        }
    }
    
    @Override
    public void notificarCambioEstado(Partido partido, String estadoAnterior) {
        if (!configurado || partido == null) return;
        
        System.out.println("=== NOTIFICACIÓN PUSH ===");
        System.out.println("Cambio de estado: " + estadoAnterior + " → " + partido.getEstado().toString());
        System.out.println("Enviando a " + partido.getJugadoresInscritos().size() + " jugadores");
    }
}