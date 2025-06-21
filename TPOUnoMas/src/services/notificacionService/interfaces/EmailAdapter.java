package services.notificacionService.interfaces;

public interface EmailAdapter {
    boolean enviarEmail(String destinatario, String asunto, String contenido);
}