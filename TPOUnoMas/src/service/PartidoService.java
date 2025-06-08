package service;

import model.Partido;
import model.Usuario;
import repository.PartidoRepository;
import repository.UsuarioRepository;
import state.NecesitamosJugadores;
import state.Cancelado;
import state.EstadoPartido;
import java.util.List;
import java.util.Map;

public class PartidoService {
    private PartidoRepository partidoRepository;
    private NotificacionService notificacionService;
    private UsuarioRepository usuarioRepository;
    
    public PartidoService(PartidoRepository partidoRepository, 
                         NotificacionService notificacionService,
                         UsuarioRepository usuarioRepository) {
        this.partidoRepository = partidoRepository;
        this.notificacionService = notificacionService;
        this.usuarioRepository = usuarioRepository;
    }
    
    public Partido crearPartido(Partido partido) {
        if (partido == null) {
            throw new IllegalArgumentException("El partido no puede ser null");
        }
        
        if (partido.getId() == null || partido.getId().trim().isEmpty()) {
            partido.setId("PAR_" + System.currentTimeMillis());
        }
        
        partido.setEstado(new NecesitamosJugadores());
        
        Partido partidoCreado = partidoRepository.guardar(partido);
        notificacionService.notificarNuevoPartido(partidoCreado);
        
        return partidoCreado;
    }
    
    public List<Partido> buscarPartidos(Map<String, Object> filtros) {
        if (filtros == null || filtros.isEmpty()) {
            return partidoRepository.buscarTodos();
        }
        return partidoRepository.buscarPorFiltros(filtros);
    }
    
    public boolean unirseAPartido(String idPartido, String idUsuario) {
        try {
            Partido partido = partidoRepository.buscarPorId(idPartido);
            if (partido == null) {
                throw new RuntimeException("Partido no encontrado: " + idPartido);
            }
            
            Usuario usuario = usuarioRepository.buscarPorId(idUsuario);
            if (usuario == null) {
                throw new RuntimeException("Usuario no encontrado: " + idUsuario);
            }
            
            if (partido.getJugadoresInscritos().contains(usuario)) {
                return false;
            }
            
            EstadoPartido estadoAnterior = partido.getEstado();
            boolean agregado = partido.getEstado().manejarNuevoJugador(partido, usuario);
            
            if (agregado) {
                partidoRepository.actualizar(partido);
                usuario.agregarPartidoAHistorial(partido);
                usuarioRepository.actualizar(usuario);
                
                if (!estadoAnterior.getClass().equals(partido.getEstado().getClass())) {
                    notificacionService.notificarCambioEstado(partido, estadoAnterior.getNombre());
                }
            }
            
            return agregado;
        } catch (Exception e) {
            throw new RuntimeException("Error al unirse al partido: " + e.getMessage(), e);
        }
    }
    
    public boolean confirmarAsistencia(String idPartido, String idUsuario) {
        try {
            Partido partido = partidoRepository.buscarPorId(idPartido);
            Usuario usuario = usuarioRepository.buscarPorId(idUsuario);
            
            if (partido == null || usuario == null) return false;
            if (!partido.getJugadoresInscritos().contains(usuario)) return false;
            
            EstadoPartido estadoAnterior = partido.getEstado();
            boolean confirmado = partido.getEstado().manejarConfirmacion(partido, usuario);
            
            if (confirmado) {
                partidoRepository.actualizar(partido);
                if (!estadoAnterior.getClass().equals(partido.getEstado().getClass())) {
                    notificacionService.notificarCambioEstado(partido, estadoAnterior.getNombre());
                }
            }
            
            return confirmado;
        } catch (Exception e) {
            throw new RuntimeException("Error al confirmar asistencia: " + e.getMessage(), e);
        }
    }
    
    public boolean cancelarPartido(String idPartido) {
        try {
            Partido partido = partidoRepository.buscarPorId(idPartido);
            if (partido == null) return false;
            
            EstadoPartido estadoAnterior = partido.getEstado();
            partido.getEstado().manejarCancelacion(partido);
            partido.setEstado(new Cancelado());
            
            partidoRepository.actualizar(partido);
            notificacionService.notificarCambioEstado(partido, estadoAnterior.getNombre());
            
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error al cancelar partido: " + e.getMessage(), e);
        }
    }
    
    public PartidoRepository getPartidoRepository() { return partidoRepository; }
    public NotificacionService getNotificacionService() { return notificacionService; }
}