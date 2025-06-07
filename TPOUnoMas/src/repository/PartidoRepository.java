package repository;

import model.Partido;
import model.Deporte;
import state.EstadoPartido;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Repositorio para gestionar la persistencia de partidos
 * Implementa operaciones CRUD para la entidad Partido
 */
public class PartidoRepository {
    
    // Simulamos una base de datos en memoria usando un Map
    private Map<String, Partido> partidos;
    
    /**
     * Constructor del repositorio de partidos
     */
    public PartidoRepository() {
        this.partidos = new ConcurrentHashMap<>();
    }
    
    /**
     * Guarda un partido en el repositorio
     * @param partido el partido a guardar
     * @return el partido guardado
     * @throws IllegalArgumentException si el partido es null o inválido
     */
    public Partido guardar(Partido partido) {
        if (partido == null) {
            throw new IllegalArgumentException("El partido no puede ser null");
        }
        
        if (partido.getId() == null || partido.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("El partido debe tener un ID válido");
        }
        
        try {
            // Clonar el partido para evitar modificaciones externas
            Partido partidoClonado = clonarPartido(partido);
            partidos.put(partido.getId(), partidoClonado);
            return partidoClonado;
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar partido: " + e.getMessage(), e);
        }
    }
    
    /**
     * Busca un partido por su ID
     * @param id el ID del partido a buscar
     * @return el partido encontrado o null si no existe
     * @throws IllegalArgumentException si el ID es null o vacío
     */
    public Partido buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID no puede ser null o vacío");
        }
        
        Partido partido = partidos.get(id);
        return partido != null ? clonarPartido(partido) : null;
    }
    
    /**
     * Busca partidos según los filtros especificados
     * @param filtros mapa con los criterios de búsqueda
     * @return lista de partidos que cumplen los criterios
     * @throws IllegalArgumentException si los filtros son null
     */
    public List<Partido> buscarPorFiltros(Map<String, Object> filtros) {
        if (filtros == null) {
            throw new IllegalArgumentException("Los filtros no pueden ser null");
        }
        
        List<Partido> partidosEncontrados = new ArrayList<>();
        
        for (Partido partido : partidos.values()) {
            if (cumpleFiltros(partido, filtros)) {
                partidosEncontrados.add(clonarPartido(partido));
            }
        }
        
        return partidosEncontrados;
    }
    
    /**
     * Busca partidos por deporte
     * @param deporte el deporte a buscar
     * @return lista de partidos del deporte especificado
     * @throws IllegalArgumentException si el deporte es null
     */
    public List<Partido> buscarPorDeporte(Deporte deporte) {
        if (deporte == null) {
            throw new IllegalArgumentException("El deporte no puede ser null");
        }
        
        List<Partido> partidosEncontrados = new ArrayList<>();
        
        for (Partido partido : partidos.values()) {
            if (partido.getDeporte() != null && 
                partido.getDeporte().getId().equals(deporte.getId())) {
                partidosEncontrados.add(clonarPartido(partido));
            }
        }
        
        return partidosEncontrados;
    }
    
    /**
     * Busca partidos por ubicación
     * @param ubicacion la ubicación a buscar
     * @return lista de partidos en esa ubicación
     * @throws IllegalArgumentException si la ubicación es null o vacía
     */
    public List<Partido> buscarPorUbicacion(String ubicacion) {
        if (ubicacion == null || ubicacion.trim().isEmpty()) {
            throw new IllegalArgumentException("La ubicación no puede ser null o vacía");
        }
        
        List<Partido> partidosEncontrados = new ArrayList<>();
        String ubicacionBusqueda = ubicacion.toLowerCase().trim();
        
        for (Partido partido : partidos.values()) {
            if (partido.getUbicacion() != null && 
                partido.getUbicacion().toLowerCase().contains(ubicacionBusqueda)) {
                partidosEncontrados.add(clonarPartido(partido));
            }
        }
        
        return partidosEncontrados;
    }
    
    /**
     * Busca partidos por estado
     * @param estado el estado a buscar
     * @return lista de partidos en ese estado
     * @throws IllegalArgumentException si el estado es null
     */
    public List<Partido> buscarPorEstado(EstadoPartido estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser null");
        }
        
        List<Partido> partidosEncontrados = new ArrayList<>();
        
        for (Partido partido : partidos.values()) {
            if (partido.getEstado() != null && 
                partido.getEstado().getClass().equals(estado.getClass())) {
                partidosEncontrados.add(clonarPartido(partido));
            }
        }
        
        return partidosEncontrados;
    }
    
    /**
     * Busca partidos por rango de fechas
     * @param fechaInicio fecha de inicio del rango
     * @param fechaFin fecha de fin del rango
     * @return lista de partidos en el rango de fechas
     * @throws IllegalArgumentException si las fechas son null o inválidas
     */
    public List<Partido> buscarPorRangoFechas(Date fechaInicio, Date fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser null");
        }
        
        if (fechaInicio.after(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        
        List<Partido> partidosEncontrados = new ArrayList<>();
        
        for (Partido partido : partidos.values()) {
            if (partido.getFechaHora() != null &&
                !partido.getFechaHora().before(fechaInicio) &&
                !partido.getFechaHora().after(fechaFin)) {
                partidosEncontrados.add(clonarPartido(partido));
            }
        }
        
        return partidosEncontrados;
    }
    
    /**
     * Actualiza un partido existente
     * @param partido el partido con los datos actualizados
     * @return el partido actualizado
     * @throws IllegalArgumentException si el partido es null o no tiene ID
     * @throws RuntimeException si el partido no existe
     */
    public Partido actualizar(Partido partido) {
        if (partido == null) {
            throw new IllegalArgumentException("El partido no puede ser null");
        }
        
        if (partido.getId() == null || partido.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("El partido debe tener un ID válido para actualizar");
        }
        
        if (!partidos.containsKey(partido.getId())) {
            throw new RuntimeException("No se encontró partido con ID: " + partido.getId());
        }
        
        try {
            Partido partidoClonado = clonarPartido(partido);
            partidos.put(partido.getId(), partidoClonado);
            return partidoClonado;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar partido: " + e.getMessage(), e);
        }
    }
    
    /**
     * Elimina un partido por su ID
     * @param id el ID del partido a eliminar
     * @return true si se eliminó exitosamente, false si no existía
     * @throws IllegalArgumentException si el ID es null o vacío
     */
    public boolean eliminar(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID no puede ser null o vacío");
        }
        
        return partidos.remove(id) != null;
    }
    
    /**
     * Obtiene todos los partidos
     * @return lista con todos los partidos registrados
     */
    public List<Partido> buscarTodos() {
        List<Partido> todosLosPartidos = new ArrayList<>();
        
        for (Partido partido : partidos.values()) {
            todosLosPartidos.add(clonarPartido(partido));
        }
        
        return todosLosPartidos;
    }
    
    /**
     * Obtiene la cantidad total de partidos registrados
     * @return el número total de partidos
     */
    public int contarPartidos() {
        return partidos.size();
    }
    
    /**
     * Verifica si existe un partido con el ID especificado
     * @param id el ID a verificar
     * @return true si existe, false en caso contrario
     */
    public boolean existePartido(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        return partidos.containsKey(id);
    }
    
    /**
     * Limpia todos los partidos del repositorio (útil para testing)
     */
    public void limpiar() {
        partidos.clear();
    }
    
    /**
     * Verifica si un partido cumple con los filtros especificados
     * @param partido el partido a verificar
     * @param filtros los filtros a aplicar
     * @return true si cumple los filtros, false en caso contrario
     */
    private boolean cumpleFiltros(Partido partido, Map<String, Object> filtros) {
        for (Map.Entry<String, Object> filtro : filtros.entrySet()) {
            String campo = filtro.getKey();
            Object valor = filtro.getValue();
            
            switch (campo.toLowerCase()) {
                case "deporte":
                    if (valor instanceof Deporte) {
                        Deporte deporteFiltro = (Deporte) valor;
                        if (partido.getDeporte() == null || 
                            !partido.getDeporte().getId().equals(deporteFiltro.getId())) {
                            return false;
                        }
                    }
                    break;
                    
                case "ubicacion":
                    if (valor instanceof String) {
                        String ubicacionFiltro = ((String) valor).toLowerCase();
                        if (partido.getUbicacion() == null || 
                            !partido.getUbicacion().toLowerCase().contains(ubicacionFiltro)) {
                            return false;
                        }
                    }
                    break;
                    
                case "estado":
                    if (valor instanceof String) {
                        String estadoFiltro = (String) valor;
                        if (partido.getEstado() == null || 
                            !partido.getEstado().getNombre().equalsIgnoreCase(estadoFiltro)) {
                            return false;
                        }
                    }
                    break;
                    
                case "fecha_desde":
                    if (valor instanceof Date) {
                        Date fechaDesde = (Date) valor;
                        if (partido.getFechaHora() == null || 
                            partido.getFechaHora().before(fechaDesde)) {
                            return false;
                        }
                    }
                    break;
                    
                case "fecha_hasta":
                    if (valor instanceof Date) {
                        Date fechaHasta = (Date) valor;
                        if (partido.getFechaHora() == null || 
                            partido.getFechaHora().after(fechaHasta)) {
                            return false;
                        }
                    }
                    break;
                    
                case "necesita_jugadores":
                    if (valor instanceof Boolean) {
                        Boolean necesitaJugadores = (Boolean) valor;
                        boolean partidoNecesitaJugadores = !partido.estaCompleto();
                        if (!necesitaJugadores.equals(partidoNecesitaJugadores)) {
                            return false;
                        }
                    }
                    break;
            }
        }
        
        return true;
    }
    
    /**
     * Clona un partido para evitar modificaciones externas
     * @param partido el partido original
     * @return una copia del partido
     */
    private Partido clonarPartido(Partido partido) {
        if (partido == null) {
            return null;
        }
        
        // Creamos una nueva instancia de Partido con los mismos datos
        Partido clon = new Partido();
        clon.setId(partido.getId());
        clon.setDeporte(partido.getDeporte());
        clon.setDuracion(partido.getDuracion());
        clon.setUbicacion(partido.getUbicacion());
        clon.setFechaHora(partido.getFechaHora() != null ? 
                         new Date(partido.getFechaHora().getTime()) : null);
        clon.setEstado(partido.getEstado());
        
        // Copiar jugadores inscritos
        if (partido.getJugadoresInscritos() != null) {
            clon.setJugadoresInscritos(new ArrayList<>(partido.getJugadoresInscritos()));
        }
        
        // Copiar notificaciones
        if (partido.getNotificaciones() != null) {
            clon.setNotificaciones(new ArrayList<>(partido.getNotificaciones()));
        }
        
        return clon;
    }
}