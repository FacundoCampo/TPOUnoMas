package service;

import model.Partido;
import model.Usuario;
import repository.PartidoRepository;
import repository.UsuarioRepository;
import state.EstadoPartido;
import state.NecesitamosJugadores;
import state.Cancelado;

import java.util.List;
import java.util.Map;
import java.util.Date;

/**
 * Servicio para gestionar la lógica de negocio relacionada con partidos
 * Implementa las operaciones CRUD y la lógica de estados
 */
public class PartidoService {
    
    private PartidoRepository partidoRepository;
    private NotificacionService notificacionService;
    private UsuarioRepository usuarioRepository;
    
    /**
     * Constructor del servicio de partido
     * @param partidoRepository repositorio para operaciones de persistencia
     * @param notificacionService servicio para envío de notificaciones
     */
    public PartidoService(PartidoRepository partidoRepository, 
                         NotificacionService notificacionService,
                         UsuarioRepository usuarioRepository) {
        this.partidoRepository = partidoRepository;
        this.notificacionService = notificacionService;
        this.usuarioRepository = usuarioRepository;
    }
    
    /**
     * Crea un nuevo partido en el sistema
     * @param partido el partido a crear
     * @return el partido creado con su ID asignado
     * @throws IllegalArgumentException si el partido es inválido
     */
    public Partido crearPartido(Partido partido) {
        if (partido == null) {
            throw new IllegalArgumentException("El partido no puede ser null");
        }
        
        // Validar datos del partido
        validarDatosPartido(partido);
        
        // Generar ID único si no tiene
        if (partido.getId() == null || partido.getId().trim().isEmpty()) {
            partido.setId(generarIdUnico());
        }
        
        // Establecer estado inicial
        partido.setEstado(new NecesitamosJugadores());
        
        try {
            // Guardar el partido
            Partido partidoCreado = partidoRepository.guardar(partido);
            
            // Notificar sobre nuevo partido
            notificacionService.notificarNuevoPartido(partidoCreado);
            
            return partidoCreado;
        } catch (Exception e) {
            throw new RuntimeException("Error al crear partido: " + e.getMessage(), e);
        }
    }
    
    /**
     * Busca partidos según los filtros especificados
     * @param filtros mapa con los criterios de búsqueda
     * @return lista de partidos que cumplen los criterios
     */
    public List<Partido> buscarPartidos(Map<String, Object> filtros) {
        try {
            if (filtros == null || filtros.isEmpty()) {
                // Si no hay filtros, retornar todos los partidos activos
                return partidoRepository.buscarTodos();
            }
            
            return partidoRepository.buscarPorFiltros(filtros);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar partidos: " + e.getMessage(), e);
        }
    }
    
    /**
     * Permite a un usuario unirse a un partido
     * @param idPartido el ID del partido
     * @param idUsuario el ID del usuario
     * @return true si se unió exitosamente, false en caso contrario
     */
    public boolean unirseAPartido(String idPartido, String idUsuario) {
        try {
            // Buscar el partido
            Partido partido = partidoRepository.buscarPorId(idPartido);
            if (partido == null) {
                throw new RuntimeException("Partido no encontrado con ID: " + idPartido);
            }
            
            // Buscar el usuario
            Usuario usuario = usuarioRepository.buscarPorId(idUsuario);
            if (usuario == null) {
                throw new RuntimeException("Usuario no encontrado con ID: " + idUsuario);
            }
            
            // Verificar si ya está inscrito
            if (partido.getJugadoresInscritos().contains(usuario)) {
                return false; // Ya está inscrito
            }
            
            // Intentar agregar el jugador usando el patrón State
            EstadoPartido estadoAnterior = partido.getEstado();
            boolean agregado = partido.getEstado().manejarNuevoJugador(partido, usuario);
            
            if (agregado) {
                // Actualizar el partido en el repositorio
                partidoRepository.actualizar(partido);
                
                // Agregar el partido al historial del usuario
                usuario.agregarPartidoAHistorial(partido);
                usuarioRepository.actualizar(usuario);
                
                // Verificar si cambió el estado y notificar
                if (!estadoAnterior.getClass().equals(partido.getEstado().getClass())) {
                    notificacionService.notificarCambioEstado(partido, estadoAnterior.getNombre());
                }
            }
            
            return agregado;
        } catch (Exception e) {
            throw new RuntimeException("Error al unirse al partido: " + e.getMessage(), e);
        }
    }
    
    /**
     * Confirma la asistencia de un usuario a un partido
     * @param idPartido el ID del partido
     * @param idUsuario el ID del usuario
     * @return true si se confirmó exitosamente, false en caso contrario
     */
    public boolean confirmarAsistencia(String idPartido, String idUsuario) {
        try {
            // Buscar el partido
            Partido partido = partidoRepository.buscarPorId(idPartido);
            if (partido == null) {
                throw new RuntimeException("Partido no encontrado con ID: " + idPartido);
            }
            
            // Buscar el usuario
            Usuario usuario = usuarioRepository.buscarPorId(idUsuario);
            if (usuario == null) {
                throw new RuntimeException("Usuario no encontrado con ID: " + idUsuario);
            }
            
            // Verificar que el usuario esté inscrito en el partido
            if (!partido.getJugadoresInscritos().contains(usuario)) {
                throw new RuntimeException("El usuario no está inscrito en este partido");
            }
            
            // Manejar confirmación usando el patrón State
            EstadoPartido estadoAnterior = partido.getEstado();
            boolean confirmado = partido.getEstado().manejarConfirmacion(partido, usuario);
            
            if (confirmado) {
                // Actualizar el partido
                partidoRepository.actualizar(partido);
                
                // Notificar cambio de estado si corresponde
                if (!estadoAnterior.getClass().equals(partido.getEstado().getClass())) {
                    notificacionService.notificarCambioEstado(partido, estadoAnterior.getNombre());
                }
            }
            
            return confirmado;
        } catch (Exception e) {
            throw new RuntimeException("Error al confirmar asistencia: " + e.getMessage(), e);
        }
    }
    
    /**
     * Cancela un partido
     * @param idPartido el ID del partido a cancelar
     * @return true si se canceló exitosamente, false en caso contrario
     */
    public boolean cancelarPartido(String idPartido) {
        try {
            // Buscar el partido
            Partido partido = partidoRepository.buscarPorId(idPartido);
            if (partido == null) {
                throw new RuntimeException("Partido no encontrado con ID: " + idPartido);
            }
            
            // Manejar cancelación usando el patrón State
            EstadoPartido estadoAnterior = partido.getEstado();
            partido.getEstado().manejarCancelacion(partido);
            
            // El estado debe cambiar a Cancelado
            partido.setEstado(new Cancelado());
            
            // Actualizar el partido
            partidoRepository.actualizar(partido);
            
            // Notificar cancelación
            notificacionService.notificarCambioEstado(partido, estadoAnterior.getNombre());
            
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error al cancelar partido: " + e.getMessage(), e);
        }
    }
    
    /**
     * Actualiza el estado de un partido
     * @param idPartido el ID del partido
     * @param nuevoEstado el nuevo estado del partido
     * @return true si se actualizó exitosamente, false en caso contrario
     */
    public boolean actualizarEstadoPartido(String idPartido, EstadoPartido nuevoEstado) {
        try {
            Partido partido = partidoRepository.buscarPorId(idPartido);
            if (partido == null) {
                throw new RuntimeException("Partido no encontrado con ID: " + idPartido);
            }
            
            EstadoPartido estadoAnterior = partido.getEstado();
            partido.setEstado(nuevoEstado);
            
            // Verificar transición usando el patrón State
            nuevoEstado.verificarTransicion(partido);
            
            // Actualizar el partido
            partidoRepository.actualizar(partido);
            
            // Notificar cambio de estado
            notificacionService.notificarCambioEstado(partido, estadoAnterior.getNombre());
            
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar estado del partido: " + e.getMessage(), e);
        }
    }
    
    /**
     * Valida los datos obligatorios del partido
     * @param partido el partido a validar
     * @throws IllegalArgumentException si algún dato es inválido
     */
    private void validarDatosPartido(Partido partido) {
        if (partido.getDeporte() == null) {
            throw new IllegalArgumentException("El deporte del partido es requerido");
        }
        
        if (partido.getUbicacion() == null || partido.getUbicacion().trim().isEmpty()) {
            throw new IllegalArgumentException("La ubicación del partido es requerida");
        }
        
        if (partido.getFechaHora() == null) {
            throw new IllegalArgumentException("La fecha y hora del partido son requeridas");
        }
        
        if (partido.getFechaHora().before(new Date())) {
            throw new IllegalArgumentException("La fecha del partido no puede ser en el pasado");
        }
        
        if (partido.getDuracion() <= 0) {
            throw new IllegalArgumentException("La duración del partido debe ser mayor a 0");
        }
    }
    
    /**
     * Genera un ID único para el partido
     * @return un ID único
     */
    private String generarIdUnico() {
        return "PAR_" + System.currentTimeMillis() + "_" + Math.random();
    }
    
    /**
     * Obtiene el repositorio de partido
     * @return el repositorio de partido
     */
    public PartidoRepository getPartidoRepository() {
        return partidoRepository;
    }
    
    /**
     * Obtiene el servicio de notificación
     * @return el servicio de notificación
     */
    public NotificacionService getNotificacionService() {
        return notificacionService;
    }
}