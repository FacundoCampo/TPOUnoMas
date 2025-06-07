package service;

import model.Partido;
import model.Usuario;
import strategy.EstrategiaEmparejamiento;

import java.util.List;

/**
 * Servicio para gestionar el emparejamiento de jugadores
 * Implementa el patrón Strategy para diferentes algoritmos de emparejamiento
 */
public class EmparejamientoService {
    
    private EstrategiaEmparejamiento estrategia;
    
    /**
     * Constructor por defecto del servicio de emparejamiento
     */
    public EmparejamientoService() {
        this.estrategia = null;
    }
    
    /**
     * Constructor del servicio de emparejamiento con estrategia inicial
     * @param estrategia la estrategia de emparejamiento inicial
     */
    public EmparejamientoService(EstrategiaEmparejamiento estrategia) {
        this.estrategia = estrategia;
    }
    
    /**
     * Establece la estrategia de emparejamiento a utilizar
     * Implementa el patrón Strategy permitiendo cambiar el algoritmo en tiempo de ejecución
     * @param estrategia la nueva estrategia de emparejamiento
     * @throws IllegalArgumentException si la estrategia es null
     */
    public void setEstrategia(EstrategiaEmparejamiento estrategia) {
        if (estrategia == null) {
            throw new IllegalArgumentException("La estrategia de emparejamiento no puede ser null");
        }
        this.estrategia = estrategia;
    }
    
    /**
     * Empareja jugadores para un partido específico usando la estrategia configurada
     * @param partido el partido para el cual emparejar jugadores
     * @param candidatos lista de usuarios candidatos para el partido
     * @return lista de usuarios seleccionados según la estrategia
     * @throws IllegalStateException si no hay estrategia configurada
     * @throws IllegalArgumentException si los parámetros son inválidos
     */
    public List<Usuario> emparejarJugadores(Partido partido, List<Usuario> candidatos) {
        if (estrategia == null) {
            throw new IllegalStateException("No se ha configurado una estrategia de emparejamiento");
        }
        
        if (partido == null) {
            throw new IllegalArgumentException("El partido no puede ser null");
        }
        
        if (candidatos == null) {
            throw new IllegalArgumentException("La lista de candidatos no puede ser null");
        }
        
        try {
            return estrategia.emparejarJugadores(partido, candidatos);
        } catch (Exception e) {
            throw new RuntimeException("Error al emparejar jugadores: " + e.getMessage(), e);
        }
    }
    
    /**
     * Verifica si un jugador es apto para un partido específico
     * @param partido el partido a evaluar
     * @param jugador el jugador candidato
     * @return true si el jugador es apto, false en caso contrario
     * @throws IllegalStateException si no hay estrategia configurada
     * @throws IllegalArgumentException si los parámetros son inválidos
     */
    public boolean esJugadorApto(Partido partido, Usuario jugador) {
        if (estrategia == null) {
            throw new IllegalStateException("No se ha configurado una estrategia de emparejamiento");
        }
        
        if (partido == null) {
            throw new IllegalArgumentException("El partido no puede ser null");
        }
        
        if (jugador == null) {
            throw new IllegalArgumentException("El jugador no puede ser null");
        }
        
        try {
            return estrategia.esJugadorApto(partido, jugador);
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar aptitud del jugador: " + e.getMessage(), e);
        }
    }
    
    /**
     * Filtra una lista de candidatos manteniendo solo los jugadores aptos
     * @param partido el partido a evaluar
     * @param candidatos lista completa de candidatos
     * @return lista filtrada con solo los jugadores aptos
     * @throws IllegalStateException si no hay estrategia configurada
     */
    public List<Usuario> filtrarCandidatosAptos(Partido partido, List<Usuario> candidatos) {
        if (estrategia == null) {
            throw new IllegalStateException("No se ha configurado una estrategia de emparejamiento");
        }
        
        if (partido == null) {
            throw new IllegalArgumentException("El partido no puede ser null");
        }
        
        if (candidatos == null) {
            throw new IllegalArgumentException("La lista de candidatos no puede ser null");
        }
        
        try {
            return candidatos.stream()
                    .filter(candidato -> estrategia.esJugadorApto(partido, candidato))
                    .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al filtrar candidatos aptos: " + e.getMessage(), e);
        }
    }
    
    /**
     * Obtiene información sobre la estrategia actual
     * @return nombre de la clase de la estrategia actual o "No configurada"
     */
    public String getInformacionEstrategia() {
        if (estrategia == null) {
            return "No configurada";
        }
        return estrategia.getClass().getSimpleName();
    }
    
    /**
     * Verifica si hay una estrategia configurada
     * @return true si hay estrategia configurada, false en caso contrario
     */
    public boolean tieneEstrategiaConfigurada() {
        return estrategia != null;
    }
    
    /**
     * Obtiene la estrategia actual (para testing o casos especiales)
     * @return la estrategia actual o null si no está configurada
     */
    public EstrategiaEmparejamiento getEstrategia() {
        return estrategia;
    }
    
    /**
     * Calcula cuántos jugadores más se necesitan para completar un partido
     * @param partido el partido a evaluar
     * @return número de jugadores faltantes
     * @throws IllegalArgumentException si el partido es null
     */
    public int calcularJugadoresFaltantes(Partido partido) {
        if (partido == null) {
            throw new IllegalArgumentException("El partido no puede ser null");
        }
        
        if (partido.getDeporte() == null) {
            throw new IllegalArgumentException("El partido debe tener un deporte definido");
        }
        
        int jugadoresRequeridos = partido.getDeporte().getCantidadJugadoresEstandar();
        int jugadoresActuales = partido.getJugadoresInscritos() != null ? 
                               partido.getJugadoresInscritos().size() : 0;
        
        return Math.max(0, jugadoresRequeridos - jugadoresActuales);
    }
    
    /**
     * Verifica si un partido está completo (tiene todos los jugadores necesarios)
     * @param partido el partido a verificar
     * @return true si está completo, false en caso contrario
     */
    public boolean estaPartidoCompleto(Partido partido) {
        return calcularJugadoresFaltantes(partido) == 0;
    }
}