package enums;

public enum TipoServicio {
    JAVA_MAIL("JavaMail"),
    SENDGRID("SendGrid"),
    OUTLOOK("Outlook");

    private final String nombre;

    TipoServicio(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() { return nombre; }
}
