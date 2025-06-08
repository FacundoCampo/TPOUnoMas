package adapter;

public interface EmailAdapter {
    boolean enviarEmail(String destinatario, String asunto, String contenido);
}