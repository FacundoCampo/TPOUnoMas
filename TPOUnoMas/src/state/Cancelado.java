package state;

import model.Partido;
import model.Usuario;
import java.util.Date;

/**
 * Estado que representa un partido que ha sido cancelado
 * Implementa el patrón State
 * Este es un estado terminal - no se puede transicionar a otros estados
 */
public class Cancelado implements EstadoPartido {
    
    private Date fechaCancelacion;
    private String motivoCancelacion;
    
    /**
     * Constructor por defecto
     */
    public Cancelado() {
        this.fechaCancelacion = new Date();
        this.motivoCancelacion = "No especificado";
    }
    
    /**
     * Constructor con motivo de cancelación
     * @param motivoCancelacion el motivo por el cual se canceló el partido
     */
    public Cancelado(String motivoCancelacion) {
        this.fechaCancelacion = new Date();
        this.motivoCancelacion = motivoCancelacion != null ? motivoCancelacion : "No especificado";
    }
    
    @Override
    public boolean manejarNuevoJugador(Partido contexto, Usuario jugador) {
        // No se pueden agregar jugadores a un partido cancelado
        System.out.println("No se pueden agregar jugadores: el partido ha sido cancelado");
        return false;
    }
    
    @Override
    public boolean manejarConfirmacion(Partido contexto, Usuario jugador) {
        // No se pueden confirmar jugadores en un partido cancelado
        System.out.println("No se puede confirmar participación: el partido ha sido cancelado");
        return false;
    }
    
    @Override
    public void manejarCancelacion(Partido contexto) {
        // El partido ya está cancelado
        System.out.println("El partido ya se encuentra cancelado desde: " + fechaCancelacion);
        System.out.println("Motivo: " + motivoCancelacion);
    }
    
    @Override
    public void verificarTransicion(Partido contexto) {
        // Estado terminal - no hay transiciones desde aquí
        System.out.println("Partido cancelado - no hay transiciones de estado disponibles");
    }
    
    @Override
    public String getNombre() {
        return "Cancelado";
    }
    
    @Override
    public boolean puedeAgregarJugadores() {
        return false;
    }
    
    @Override
    public boolean puedeCancelar() {
        return false; // Ya está cancelado
    }
    
    @Override
    public boolean puedeConfirmar() {
        return false;
    }
    
    /**
     * Obtiene la fecha de cancelación
     * @return la fecha cuando se canceló el partido
     */
    public Date getFechaCancelacion() {
        return fechaCancelacion != null ? new Date(fechaCancelacion.getTime()) : null;
    }
    
    /**
     * Establece la fecha de cancelación
     * @param fechaCancelacion la fecha de cancelación
     */
    public void setFechaCancelacion(Date fechaCancelacion) {
        this.fechaCancelacion = fechaCancelacion != null ? new Date(fechaCancelacion.getTime()) : new Date();
    }
    
    /**
     * Obtiene el motivo de cancelación
     * @return el motivo por el cual se canceló
     */
    public String getMotivoCancelacion() {
        return motivoCancelacion;
    }
    
    /**
     * Establece el motivo de cancelación
     * @param motivoCancelacion el nuevo motivo
     */
    public void setMotivoCancelacion(String motivoCancelacion) {
        this.motivoCancelacion = motivoCancelacion != null ? motivoCancelacion : "No especificado";
    }
    
    /**
     * Verifica si el partido fue cancelado por falta de jugadores
     * @return true si fue cancelado por falta de jugadores
     */
    public boolean fueCanceladoPorFaltaDeJugadores() {
        return motivoCancelacion != null && 
               motivoCancelacion.toLowerCase().contains("falta de jugadores");
    }
    
    /**
     * Verifica si el partido fue cancelado por mal tiempo
     * @return true si fue cancelado por condiciones climáticas
     */
    public boolean fueCanceladoPorMalTiempo() {
        return motivoCancelacion != null && 
               (motivoCancelacion.toLowerCase().contains("lluvia") ||
                motivoCancelacion.toLowerCase().contains("clima") ||
                motivoCancelacion.toLowerCase().contains("tiempo"));
    }
    
    /**
     * Verifica si se puede crear un nuevo partido como reemplazo
     * @return true si se puede crear un partido de reemplazo
     */
    public boolean puedeCrearReemplazo() {
        return true; // Un partido cancelado puede ser reemplazado por uno nuevo
    }
    
    /**
     * Obtiene información completa de la cancelación
     * @param contexto el partido cancelado
     * @return string con información detallada de la cancelación
     */
    public String getInformacionCancelacion(Partido contexto) {
        StringBuilder info = new StringBuilder();
        info.append("Partido cancelado\n");
        
        if (contexto.getDeporte() != null) {
            info.append("Deporte: ").append(contexto.getDeporte().getNombre()).append("\n");
        }
        
        info.append("Ubicación: ").append(contexto.getUbicacion()).append("\n");
        info.append("Fecha original: ").append(contexto.getFechaHora()).append("\n");
        info.append("Fecha de cancelación: ").append(fechaCancelacion).append("\n");
        info.append("Motivo: ").append(motivoCancelacion).append("\n");
        info.append("Jugadores que estaban inscritos: ").append(contexto.getJugadoresInscritos().size());
        
        return info.toString();
    }
    
    /**
     * Notifica a todos los jugadores sobre la cancelación
     * @param contexto el partido cancelado
     */
    public void notificarCancelacion(Partido contexto) {
        System.out.println("Notificando cancelación a " + contexto.getJugadoresInscritos().size() + " jugadores");
        System.out.println("Motivo de cancelación: " + motivoCancelacion);
        
        // En una implementación real, aquí se enviarían las notificaciones
        // usando el servicio de notificaciones
    }
    
    /**
     * Verifica si la cancelación fue reciente (menos de 24 horas)
     * @return true si fue cancelado recientemente
     */
    public boolean fueCanceladoRecientemente() {
        if (fechaCancelacion == null) {
            return false;
        }
        
        long tiempoActual = System.currentTimeMillis();
        long tiempoCancelacion = fechaCancelacion.getTime();
        long diferencia = tiempoActual - tiempoCancelacion;
        
        // 24 horas en milisegundos
        long unDiaEnMs = 24 * 60 * 60 * 1000;
        
        return diferencia < unDiaEnMs;
    }
    
    @Override
    public String toString() {
        return "Estado: " + getNombre() + " (Fecha: " + fechaCancelacion + ", Motivo: " + motivoCancelacion + ")";
    }
}