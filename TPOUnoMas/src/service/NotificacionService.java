package service;

import model.Partido;
import model.Usuario;
import model.Notificacion;
import observer.StrategyNotificacion;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

/**
 * Servicio para gestionar las notificaciones del sistema
 * Implementa el patrón Observer para notificar cambios a los observadores registrados
 */
public class NotificacionService {
    
    private List<StrategyNotificacion> observadores;
    
    /**
     * Constructor del servicio de notificaciones
     * Inicializa la lista de observadores
     */
    public NotificacionService() {
        this.observadores = new ArrayList<>();
    }
    
    /**
     * Agrega un observador a la lista de notificadores
     * Implementa el patrón Observer
     * @param observador el observador a agregar
     * @throws IllegalArgumentException si el observador es null
     */
    public void agregarObservador(StrategyNotificacion observador) {
        if (observador == null) {
            throw new IllegalArgumentException("El observador no puede ser null");
        }
        
        if (!observadores.contains(observador)) {
            observadores.add(observador);
        }
    }
    
    /**
     * Elimina un observador de la lista de notificadores
     * @param observador el observador a eliminar
     * @throws IllegalArgumentException si el observador es null
     */
    public void eliminarObservador(StrategyNotificacion observador) {
        if (observador == null) {
            throw new IllegalArgumentException("El observador no puede ser null");
        }
        
        observadores.remove(observador);
    }
    
    /**
     * Notifica a todos los observadores sobre un cambio de estado en un partido
     * @param partido el partido que cambió de estado
     * @param estadoAnterior el estado anterior del partido
     * @throws IllegalArgumentException si el partido es null
     */
    public void notificarCambioEstado(Partido partido, String estadoAnterior) {
        if (partido == null) {
            throw new IllegalArgumentException("El partido no puede ser null");
        }
        
        if (estadoAnterior == null) {
            estadoAnterior = "Desconocido";
        }
        
        try {
            // Crear notificación para cada jugador inscrito
            List<Usuario> jugadoresANotificar = partido.getJugadoresInscritos();
            if (jugadoresANotificar != null && !jugadoresANotificar.isEmpty()) {
                
                // Crear notificaciones individuales
                for (Usuario jugador : jugadoresANotificar) {
                    Notificacion notificacion = crearNotificacionCambioEstado(
                        partido, jugador, estadoAnterior);
                    partido.agregarNotificacion(notificacion);
                }
                
                // Notificar a todos los observadores
                for (StrategyNotificacion observador : observadores) {
                    observador.notificarCambioEstado(partido, estadoAnterior);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al notificar cambio de estado: " + e.getMessage());
            // En un sistema real, esto se loggearía apropiadamente
        }
    }
    
    /**
     * Notifica a todos los observadores sobre un nuevo partido creado
     * @param partido el nuevo partido creado
     * @throws IllegalArgumentException si el partido es null
     */
    public void notificarNuevoPartido(Partido partido) {
        if (partido == null) {
            throw new IllegalArgumentException("El partido no puede ser null");
        }
        
        try {
            // Obtener usuarios interesados en el deporte del partido
            List<Usuario> usuariosInteresados = obtenerUsuariosInteresados(partido);
            
            if (usuariosInteresados != null && !usuariosInteresados.isEmpty()) {
                
                // Crear notificaciones para usuarios interesados
                for (Usuario usuario : usuariosInteresados) {
                    Notificacion notificacion = crearNotificacionNuevoPartido(partido, usuario);
                    partido.agregarNotificacion(notificacion);
                }
                
                // Notificar a todos los observadores
                for (StrategyNotificacion observador : observadores) {
                    observador.notificarNuevoPartido(partido, usuariosInteresados);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al notificar nuevo partido: " + e.getMessage());
            // En un sistema real, esto se loggearía apropiadamente
        }
    }
    
    /**
     * Notifica sobre la confirmación de un partido
     * @param partido el partido confirmado
     */
    public void notificarPartidoConfirmado(Partido partido) {
        if (partido == null) {
            throw new IllegalArgumentException("El partido no puede ser null");
        }
        
        try {
            List<Usuario> jugadoresInscritos = partido.getJugadoresInscritos();
            if (jugadoresInscritos != null && !jugadoresInscritos.isEmpty()) {
                
                // Crear notificaciones de confirmación
                for (Usuario jugador : jugadoresInscritos) {
                    Notificacion notificacion = crearNotificacionConfirmacion(partido, jugador);
                    partido.agregarNotificacion(notificacion);
                }
                
                // Notificar a observadores (reutilizamos el método de cambio de estado)
                for (StrategyNotificacion observador : observadores) {
                    observador.notificarCambioEstado(partido, "Partido Armado");
                }
            }
        } catch (Exception e) {
            System.err.println("Error al notificar confirmación de partido: " + e.getMessage());
        }
    }
    
    /**
     * Crea una notificación para un cambio de estado
     * @param partido el partido que cambió de estado
     * @param usuario el usuario a notificar
     * @param estadoAnterior el estado anterior del partido
     * @return la notificación creada
     */
    private Notificacion crearNotificacionCambioEstado(Partido partido, Usuario usuario, String estadoAnterior) {
        String mensaje = String.format(
            "El partido de %s programado para %s ha cambiado de estado de '%s' a '%s'",
            partido.getDeporte().getNombre(),
            partido.getFechaHora().toString(),
            estadoAnterior,
            partido.getEstado().getNombre()
        );
        
        return new Notificacion(
            generarIdNotificacion(),
            partido.getId(),
            usuario.getId(),
            mensaje,
            new Date(),
            false
        );
    }
    
    /**
     * Crea una notificación para un nuevo partido
     * @param partido el nuevo partido
     * @param usuario el usuario a notificar
     * @return la notificación creada
     */
    private Notificacion crearNotificacionNuevoPartido(Partido partido, Usuario usuario) {
        String mensaje = String.format(
            "¡Nuevo partido de %s disponible! Ubicación: %s, Fecha: %s. ¡Únete ahora!",
            partido.getDeporte().getNombre(),
            partido.getUbicacion(),
            partido.getFechaHora().toString()
        );
        
        return new Notificacion(
            generarIdNotificacion(),
            partido.getId(),
            usuario.getId(),
            mensaje,
            new Date(),
            false
        );
    }
    
    /**
     * Crea una notificación de confirmación de partido
     * @param partido el partido confirmado
     * @param usuario el usuario a notificar
     * @return la notificación creada
     */
    private Notificacion crearNotificacionConfirmacion(Partido partido, Usuario usuario) {
        String mensaje = String.format(
            "¡El partido de %s está confirmado! Ubicación: %s, Fecha: %s. ¡Nos vemos allí!",
            partido.getDeporte().getNombre(),
            partido.getUbicacion(),
            partido.getFechaHora().toString()
        );
        
        return new Notificacion(
            generarIdNotificacion(),
            partido.getId(),
            usuario.getId(),
            mensaje,
            new Date(),
            false
        );
    }
    
    /**
     * Obtiene la lista de usuarios interesados en un deporte específico
     * En un sistema real, esto consultaría una base de datos
     * @param partido el partido para obtener usuarios interesados
     * @return lista de usuarios interesados en el deporte del partido
     */
    private List<Usuario> obtenerUsuariosInteresados(Partido partido) {
        // Implementación simplificada
        // En un sistema real, esto consultaría el repositorio de usuarios
        // filtrando por deporte favorito
        List<Usuario> usuariosInteresados = new ArrayList<>();
        
        // Por ahora retornamos una lista vacía ya que no tenemos acceso al repositorio
        // En la implementación completa, se haría:
        // return usuarioRepository.buscarPorDeporteFavorito(partido.getDeporte());
        
        return usuariosInteresados;
    }
    
    /**
     * Genera un ID único para la notificación
     * @return un ID único
     */
    private String generarIdNotificacion() {
        return "NOT_" + System.currentTimeMillis() + "_" + Math.random();
    }
    
    /**
     * Obtiene la cantidad de observadores registrados
     * @return número de observadores registrados
     */
    public int getCantidadObservadores() {
        return observadores.size();
    }
    
    /**
     * Verifica si hay observadores registrados
     * @return true si hay observadores, false en caso contrario
     */
    public boolean tieneObservadores() {
        return !observadores.isEmpty();
    }
    
    /**
     * Limpia todos los observadores registrados
     */
    public void limpiarObservadores() {
        observadores.clear();
    }
    
    /**
     * Obtiene una copia de la lista de observadores (para testing)
     * @return lista inmutable de observadores
     */
    public List<StrategyNotificacion> getObservadores() {
        return new ArrayList<>(observadores);
    }
    
    /**
     * Notifica a un observador específico sobre un cambio de estado
     * Útil para notificaciones dirigidas
     * @param observador el observador específico a notificar
     * @param partido el partido que cambió
     * @param estadoAnterior el estado anterior
     */
    public void notificarObservadorEspecifico(StrategyNotificacion observador, 
                                            Partido partido, String estadoAnterior) {
        if (observador == null || partido == null) {
            throw new IllegalArgumentException("El observador y el partido no pueden ser null");
        }
        
        try {
            observador.notificarCambioEstado(partido, estadoAnterior);
        } catch (Exception e) {
            System.err.println("Error al notificar observador específico: " + e.getMessage());
        }
    }
}