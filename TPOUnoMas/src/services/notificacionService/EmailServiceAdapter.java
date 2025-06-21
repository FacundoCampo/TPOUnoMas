package services.notificacionService;

import enums.TipoServicio;
import services.notificacionService.interfaces.EmailAdapter;

public class EmailServiceAdapter implements EmailAdapter {
    private TipoServicio tipoServicio;
    private boolean autenticado;
    private String usuario;

    public EmailServiceAdapter() {
        this.tipoServicio = TipoServicio.JAVA_MAIL;
        this.autenticado = false;
    }
    
    public EmailServiceAdapter(TipoServicio tipoServicio) {
        this.tipoServicio = tipoServicio;
        this.autenticado = false;
    }

    @Override
    public boolean enviarEmail(String destinatario, String asunto, String contenido) {
        if (destinatario == null || asunto == null || contenido == null) {
            System.err.println("Error: Parámetros de email inválidos");
            return false;
        }
        
        // Simulación de envío
        System.out.println("📧 Email enviado via " + tipoServicio.getNombre());
        System.out.println("   Para: " + destinatario);
        System.out.println("   Asunto: " + asunto);
        System.out.println("   ✓ Enviado exitosamente");
        
        return true;
    }
    
    public TipoServicio getTipoServicio() { return tipoServicio; }
    public boolean estaAutenticado() { return autenticado; }
}