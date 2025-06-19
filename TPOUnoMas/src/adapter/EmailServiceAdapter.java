package adapter;

public class EmailServiceAdapter implements EmailAdapter {
    private TipoServicio tipoServicio;
    private boolean autenticado;
    private String usuario;
    
    public enum TipoServicio {
        JAVA_MAIL("JavaMail", "facundo@gmail.com", 587),
        SENDGRID("SendGrid", "facundo@sendgrid.net", 587),
        OUTLOOK("Outlook", "facundo@outlook.com", 587);
        
        private final String nombre;
        private final String servidor;
        private final int puerto;
        
        TipoServicio(String nombre, String servidor, int puerto) {
            this.nombre = nombre;
            this.servidor = servidor;
            this.puerto = puerto;
        }
        
        public String getNombre() { return nombre; }
        public String getServidor() { return servidor; }
        public int getPuerto() { return puerto; }
    }
    
    public EmailServiceAdapter() {
        this.tipoServicio = TipoServicio.JAVA_MAIL;
        this.autenticado = false;
    }
    
    public EmailServiceAdapter(TipoServicio tipoServicio) {
        this.tipoServicio = tipoServicio;
        this.autenticado = false;
    }
    
    public boolean autenticar(String usuario, String contraseña) {
        if (usuario != null && contraseña != null && !usuario.trim().isEmpty()) {
            this.usuario = usuario;
            this.autenticado = true;
            System.out.println("✓ Autenticado en " + tipoServicio.getNombre() + " como: " + usuario);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean enviarEmail(String destinatario, String asunto, String contenido) {
        if (!autenticado) {
            System.err.println("Error: Servicio de email no autenticado");
            return false;
        }
        
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