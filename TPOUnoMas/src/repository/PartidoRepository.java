package repository;

import model.Partido;
import model.Deporte;
import state.EstadoPartido;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PartidoRepository {
    private Map<String, Partido> partidos;
    
    public PartidoRepository() {
        this.partidos = new ConcurrentHashMap<>();
    }
    
    public Partido guardar(Partido partido) {
        if (partido == null || partido.getId() == null) {
            throw new IllegalArgumentException("Partido o ID inválido");
        }
        partidos.put(partido.getId(), clonarPartido(partido));
        return clonarPartido(partido);
    }
    
    public Partido buscarPorId(String id) {
        if (id == null) return null;
        Partido partido = partidos.get(id);
        return partido != null ? clonarPartido(partido) : null;
    }
    
    public List<Partido> buscarPorFiltros(Map<String, Object> filtros) {
        if (filtros == null) return new ArrayList<>();
        
        List<Partido> resultado = new ArrayList<>();
        for (Partido partido : partidos.values()) {
            if (cumpleFiltros(partido, filtros)) {
                resultado.add(clonarPartido(partido));
            }
        }
        return resultado;
    }
    
    public Partido actualizar(Partido partido) {
        if (partido == null || partido.getId() == null) {
            throw new IllegalArgumentException("Partido inválido");
        }
        if (!partidos.containsKey(partido.getId())) {
            throw new RuntimeException("Partido no encontrado: " + partido.getId());
        }
        partidos.put(partido.getId(), clonarPartido(partido));
        return clonarPartido(partido);
    }
    
    public boolean eliminar(String id) {
        return id != null && partidos.remove(id) != null;
    }
    
    public List<Partido> buscarTodos() {
        List<Partido> resultado = new ArrayList<>();
        for (Partido partido : partidos.values()) {
            resultado.add(clonarPartido(partido));
        }
        return resultado;
    }
    
    public int contarPartidos() {
        return partidos.size();
    }
    
    public boolean existePartido(String id) {
        return id != null && partidos.containsKey(id);
    }
    
    public void limpiar() {
        partidos.clear();
    }
    
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
            }
        }
        return true;
    }
    
    private Partido clonarPartido(Partido partido) {
        if (partido == null) return null;
        
        Partido clon = new Partido();
        clon.setId(partido.getId());
        clon.setDeporte(partido.getDeporte());
        clon.setDuracion(partido.getDuracion());
        clon.setUbicacion(partido.getUbicacion());
        clon.setFechaHora(partido.getFechaHora());
        clon.setEstado(partido.getEstado());
        
        if (partido.getJugadoresInscritos() != null) {
            clon.setJugadoresInscritos(new ArrayList<>(partido.getJugadoresInscritos()));
        }
        
        if (partido.getNotificaciones() != null) {
            clon.setNotificaciones(new ArrayList<>(partido.getNotificaciones()));
        }
        
        return clon;
    }
}