package controller;

import model.Partido;
import model.Usuario;
import service.PartidoService;
import service.EmparejamientoService;
import service.UsuarioService;
import strategy.EstrategiaEmparejamiento;

import java.util.List;
import java.util.Map;

/**
 * Controlador para gestionar las operaciones relacionadas con partidos
 * Implementa el patrón MVC como capa de control
 */
public class PartidoController {
    
    private PartidoService partidoService;
    private EmparejamientoService emparejamientoService;
    private UsuarioService usuarioService;
    
    /**
     * Constructor del controlador de partidos
     * @param partidoService servicio para operaciones de partido
     * @param emparejamientoService servicio para emparejamiento de jugadores
     * @param usuarioService servicio para operaciones de usuario
     */
    public PartidoController(PartidoService partidoService, 
                           EmparejamientoService emparejamientoService,
                           UsuarioService usuarioService) {
        if (partidoService == null) {
            throw new IllegalArgumentException("PartidoService no puede ser null");
        }
        if (emparejamientoService == null) {
            throw new IllegalArgumentException("EmparejamientoService no puede ser null");
        }
        if (usuarioService == null) {
            throw new IllegalArgumentException("UsuarioService no puede ser null");
        }
        
        this.partidoService = partidoService;
        this.emparejamientoService = emparejamientoService;
        this.usuarioService = usuarioService;
    }
    
    /**
     * Crea un nuevo partido en el sistema con una estrategia de emparejamiento
     * @param partido el partido a crear
     * @param estrategia la estrategia de emparejamiento a usar
     * @return el partido creado con su ID asignado
     * @throws IllegalArgumentException si los parámetros son inválidos
     */
    public Partido crearPartido(Partido partido, EstrategiaEmparejamiento estrategia) {
        if (partido == null) {
            throw new IllegalArgumentException("El partido no puede ser null");
        }
        
        if (estrategia == null) {
            throw new IllegalArgumentException("La estrategia de emparejamiento no puede ser null");
        }
        
        if (partido.getDeporte() == null) {
            throw new IllegalArgumentException("El deporte del partido es requerido");
        }
        
        if (partido.getUbicacion() == null || partido.getUbicacion().trim().isEmpty()) {
            throw new IllegalArgumentException("La ubicación del partido es requerida");
        }
        
        try {
            // Establecer la estrategia de emparejamiento
            emparejamientoService.setEstrategia(estrategia);
            
            // Crear el partido
            return partidoService.crearPartido(partido);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear partido: " + e.getMessage(), e);
        }
    }
    
    /**
     * Busca partidos según los filtros especificados
     * @param filtros mapa con los criterios de búsqueda
     * @return lista de partidos que cumplen los criterios
     * @throws RuntimeException si hay error en la búsqueda
     */
    public List<Partido> buscarPartidos(Map<String, Object> filtros) {
        try {
            return partidoService.buscarPartidos(filtros);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar partidos: " + e.getMessage(), e);
        }
    }
    
    /**
     * Permite a un usuario unirse a un partido
     * @param idPartido el ID del partido
     * @param idUsuario el ID del usuario
     * @return true si se unió exitosamente, false en caso contrario
     * @throws IllegalArgumentException si los IDs son inválidos
     */
    public boolean unirseAPartido(String idPartido, String idUsuario) {
        if (idPartido == null || idPartido.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del partido no puede ser null o vacío");
        }
        
        if (idUsuario == null || idUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del usuario no puede ser null o vacío");
        }
        
        try {
            return partidoService.unirseAPartido(idPartido, idUsuario);
        } catch (Exception e) {
            throw new RuntimeException("Error al unirse al partido: " + e.getMessage(), e);
        }
    }
    
    /**
     * Confirma la asistencia de un usuario a un partido
     * @param idPartido el ID del partido
     * @param idUsuario el ID del usuario
     * @return true si se confirmó exitosamente, false en caso contrario
     * @throws IllegalArgumentException si los IDs son inválidos
     */
    public boolean confirmarAsistencia(String idPartido, String idUsuario) {
        if (idPartido == null || idPartido.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del partido no puede ser null o vacío");
        }
        
        if (idUsuario == null || idUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del usuario no puede ser null o vacío");
        }
        
        try {
            return partidoService.confirmarAsistencia(idPartido, idUsuario);
        } catch (Exception e) {
            throw new RuntimeException("Error al confirmar asistencia: " + e.getMessage(), e);
        }
    }
    
    /**
     * Cancela un partido
     * @param idPartido el ID del partido a cancelar
     * @return true si se canceló exitosamente, false en caso contrario
     * @throws IllegalArgumentException si el ID del partido es inválido
     */
    public boolean cancelarPartido(String idPartido) {
        if (idPartido == null || idPartido.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del partido no puede ser null o vacío");
        }
        
        try {
            return partidoService.cancelarPartido(idPartido);
        } catch (Exception e) {
            throw new RuntimeException("Error al cancelar partido: " + e.getMessage(), e);
        }
    }
    
    /**
     * Registra un nuevo usuario (delegado al servicio de usuario)
     * @param usuario el usuario a registrar
     * @return el usuario registrado
     * @throws IllegalArgumentException si el usuario es null
     */
    public Usuario registrarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser null");
        }
        
        // Validaciones adicionales
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del usuario es requerido");
        }
        
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email del usuario es requerido");
        }
        
        try {
            return usuarioService.registrarUsuario(usuario);
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar usuario: " + e.getMessage(), e);
        }
    }
    
    /**
     * Establece la estrategia de emparejamiento
     * @param estrategia la nueva estrategia de emparejamiento
     * @throws IllegalArgumentException si la estrategia es null
     */
    public void setEstrategiaEmparejamiento(EstrategiaEmparejamiento estrategia) {
        if (estrategia == null) {
            throw new IllegalArgumentException("La estrategia de emparejamiento no puede ser null");
        }
        
        try {
            emparejamientoService.setEstrategia(estrategia);
        } catch (Exception e) {
            throw new RuntimeException("Error al establecer estrategia: " + e.getMessage(), e);
        }
    }
    
    /**
     * Obtiene candidatos aptos para un partido usando la estrategia actual
     * @param idPartido el ID del partido
     * @param candidatos lista de usuarios candidatos
     * @return lista de usuarios aptos para el partido
     * @throws IllegalArgumentException si los parámetros son inválidos
     */
    public List<Usuario> obtenerCandidatosAptos(String idPartido, List<Usuario> candidatos) {
        if (idPartido == null || idPartido.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del partido no puede ser null o vacío");
        }
        
        if (candidatos == null) {
            throw new IllegalArgumentException("La lista de candidatos no puede ser null");
        }
        
        try {
            // Buscar el partido
            List<Partido> partidos = partidoService.buscarPartidos(null);
            Partido partido = partidos.stream()
                .filter(p -> idPartido.equals(p.getId()))
                .findFirst()
                .orElse(null);
                
            if (partido == null) {
                throw new RuntimeException("Partido no encontrado: " + idPartido);
            }
            
            return emparejamientoService.filtrarCandidatosAptos(partido, candidatos);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener candidatos aptos: " + e.getMessage(), e);
        }
    }
    
    /**
     * Ejecuta un emparejamiento completo para un partido
     * @param idPartido el ID del partido
     * @param candidatos lista de usuarios candidatos
     * @return lista de usuarios seleccionados por la estrategia
     * @throws IllegalArgumentException si los parámetros son inválidos
     */
    public List<Usuario> ejecutarEmparejamiento(String idPartido, List<Usuario> candidatos) {
        if (idPartido == null || idPartido.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del partido no puede ser null o vacío");
        }
        
        if (candidatos == null) {
            throw new IllegalArgumentException("La lista de candidatos no puede ser null");
        }
        
        try {
            // Buscar el partido
            List<Partido> partidos = partidoService.buscarPartidos(null);
            Partido partido = partidos.stream()
                .filter(p -> idPartido.equals(p.getId()))
                .findFirst()
                .orElse(null);
                
            if (partido == null) {
                throw new RuntimeException("Partido no encontrado: " + idPartido);
            }
            
            return emparejamientoService.emparejarJugadores(partido, candidatos);
        } catch (Exception e) {
            throw new RuntimeException("Error al ejecutar emparejamiento: " + e.getMessage(), e);
        }
    }
    
    /**
     * Obtiene información sobre la estrategia actual de emparejamiento
     * @return información de la estrategia configurada
     */
    public String getInformacionEstrategia() {
        try {
            return emparejamientoService.getInformacionEstrategia();
        } catch (Exception e) {
            return "Error al obtener información de estrategia: " + e.getMessage();
        }
    }
    
    /**
     * Verifica si hay una estrategia de emparejamiento configurada
     * @return true si hay estrategia configurada, false en caso contrario
     */
    public boolean tieneEstrategiaConfigurada() {
        try {
            return emparejamientoService.tieneEstrategiaConfigurada();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Busca un partido específico por su ID
     * @param idPartido el ID del partido a buscar
     * @return el partido encontrado o null si no existe
     * @throws IllegalArgumentException si el ID es inválido
     */
    public Partido buscarPartidoPorId(String idPartido) {
        if (idPartido == null || idPartido.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del partido no puede ser null o vacío");
        }
        
        try {
            List<Partido> partidos = partidoService.buscarPartidos(null);
            return partidos.stream()
                .filter(p -> idPartido.equals(p.getId()))
                .findFirst()
                .orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar partido: " + e.getMessage(), e);
        }
    }
    
    /**
     * Obtiene el servicio de partido
     * @return el servicio de partido
     */
    public PartidoService getPartidoService() {
        return partidoService;
    }
    
    /**
     * Obtiene el servicio de emparejamiento
     * @return el servicio de emparejamiento
     */
    public EmparejamientoService getEmparejamientoService() {
        return emparejamientoService;
    }
    
    /**
     * Obtiene el servicio de usuario
     * @return el servicio de usuario
     */
    public UsuarioService getUsuarioService() {
        return usuarioService;
    }
    
    /**
     * Establece el servicio de partido (para testing)
     * @param partidoService el nuevo servicio de partido
     */
    public void setPartidoService(PartidoService partidoService) {
        if (partidoService == null) {
            throw new IllegalArgumentException("PartidoService no puede ser null");
        }
        this.partidoService = partidoService;
    }
    
    /**
     * Establece el servicio de emparejamiento (para testing)
     * @param emparejamientoService el nuevo servicio de emparejamiento
     */
    public void setEmparejamientoService(EmparejamientoService emparejamientoService) {
        if (emparejamientoService == null) {
            throw new IllegalArgumentException("EmparejamientoService no puede ser null");
        }
        this.emparejamientoService = emparejamientoService;
    }
    
    /**
     * Establece el servicio de usuario (para testing)
     * @param usuarioService el nuevo servicio de usuario
     */
    public void setUsuarioService(UsuarioService usuarioService) {
        if (usuarioService == null) {
            throw new IllegalArgumentException("UsuarioService no puede ser null");
        }
        this.usuarioService = usuarioService;
    }
    
    @Override
    public String toString() {
        return String.format("PartidoController{estrategia=%s, servicios=[%s, %s, %s]}", 
                           getInformacionEstrategia(),
                           partidoService != null ? "PartidoService" : "null",
                           emparejamientoService != null ? "EmparejamientoService" : "null",
                           usuarioService != null ? "UsuarioService" : "null");
    }
}