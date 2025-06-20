package strategy;

import model.Deporte;
import model.NivelJuego;
import model.Partido;
import model.Usuario;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Estrategia de emparejamiento basada en el nivel de juego de los usuarios
 * Filtra jugadores según un rango de niveles (mínimo y máximo)
 * Implementa el patrón Strategy
 */
public class EmparejamientoPorNivel implements IEstrategiaEmparejamiento {
    
    private NivelJuego nivelMinimo;
    private NivelJuego nivelMaximo;
    
    /**
     * Constructor que permite cualquier nivel de juego
     */
    public EmparejamientoPorNivel() {
        this.nivelMinimo = NivelJuego.PRINCIPIANTE;
        this.nivelMaximo = NivelJuego.AVANZADO;
    }
    
    /**
     * Constructor con rango específico de niveles
     * @param nivelMinimo nivel mínimo requerido
     * @param nivelMaximo nivel máximo permitido
     * @throws IllegalArgumentException si los niveles son inválidos
     */
    public EmparejamientoPorNivel(NivelJuego nivelMinimo, NivelJuego nivelMaximo) {
        validarNiveles(nivelMinimo, nivelMaximo);
        this.nivelMinimo = nivelMinimo;
        this.nivelMaximo = nivelMaximo;
    }
    
    @Override
    public List<Usuario> emparejarJugadores(Partido partido, List<Usuario> candidatos) {
        if (partido == null) {
            throw new IllegalArgumentException("El partido no puede ser null");
        }
        
        if (candidatos == null) {
            throw new IllegalArgumentException("La lista de candidatos no puede ser null");
        }
        
        if (partido.getDeporte() == null) {
            throw new IllegalArgumentException("El partido debe tener un deporte definido");
        }
        
        try {
            // Filtrar candidatos que cumplan con el nivel requerido
            List<Usuario> candidatosAptos = candidatos.stream()
                .filter(candidato -> esJugadorApto(partido, candidato))
                .collect(Collectors.toList());
            
            // Calcular cuántos jugadores se necesitan
            int jugadoresFaltantes = calcularJugadoresFaltantes(partido);
            
            // Retornar los primeros N jugadores aptos (donde N = jugadoresFaltantes)
            if (candidatosAptos.size() <= jugadoresFaltantes) {
                return new ArrayList<>(candidatosAptos);
            } else {
                return candidatosAptos.subList(0, jugadoresFaltantes);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al emparejar jugadores por nivel: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean esJugadorApto(Partido partido, Usuario jugador) {
        if (partido == null) {
            throw new IllegalArgumentException("El partido no puede ser null");
        }
        
        if (jugador == null) {
            throw new IllegalArgumentException("El jugador no puede ser null");
        }
        
        if (partido.getDeporte() == null) {
            throw new IllegalArgumentException("El partido debe tener un deporte definido");
        }
        
        try {
            // Verificar que el jugador no esté ya inscrito
            if (partido.getJugadoresInscritos() != null && 
                partido.getJugadoresInscritos().contains(jugador)) {
                return false;
            }
            
            // Obtener el nivel del jugador para el deporte específico
            NivelJuego nivelJugador = jugador.getNivelJuegoParaDeporte(partido.getDeporte());
            
            // Si el jugador no tiene configurado este deporte, no es apto
            if (nivelJugador == null) {
                return false;
            }
            
            // Verificar si el nivel está dentro del rango permitido
            return cumpleRequisitosNivel(jugador, partido.getDeporte());
        } catch (Exception e) {
            // En caso de error, consideramos que no es apto
            return false;
        }
    }
    
    /**
     * Verifica si un usuario cumple con los requisitos de nivel para un deporte
     * @param usuario el usuario a verificar
     * @param deporte el deporte a evaluar
     * @return true si cumple con los requisitos, false en caso contrario
     */
    public boolean cumpleRequisitosNivel(Usuario usuario, Deporte deporte) {
        if (usuario == null || deporte == null) {
            return false;
        }
        
        try {
            NivelJuego nivelUsuario = usuario.getNivelJuegoParaDeporte(deporte);
            if (nivelUsuario == null) {
                return false;
            }
            
            // Verificar que el nivel esté dentro del rango [nivelMinimo, nivelMaximo]
            return estaEnRango(nivelUsuario, nivelMinimo, nivelMaximo);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Verifica si un nivel está dentro de un rango específico
     * @param nivel el nivel a verificar
     * @param minimo nivel mínimo del rango
     * @param maximo nivel máximo del rango
     * @return true si está en el rango, false en caso contrario
     */
    private boolean estaEnRango(NivelJuego nivel, NivelJuego minimo, NivelJuego maximo) {
        if (nivel == null || minimo == null || maximo == null) {
            return false;
        }
        
        try {
            // Usar el método del enum NivelJuego para verificar rango
            return nivel.estaEnRango(minimo, maximo);
        } catch (Exception e) {
            // Fallback: usar ordinal() si el método estaEnRango() no funciona
            int valorNivel = nivel.ordinal();
            int valorMinimo = minimo.ordinal();
            int valorMaximo = maximo.ordinal();
            
            return valorNivel >= valorMinimo && valorNivel <= valorMaximo;
        }
    }
    
    /**
     * Calcula cuántos jugadores faltan para completar el partido
     * @param partido el partido a evaluar
     * @return número de jugadores faltantes
     */
    private int calcularJugadoresFaltantes(Partido partido) {
        if (partido == null || partido.getDeporte() == null) {
            return 0;
        }
        
        try {
            int jugadoresRequeridos = partido.getDeporte().getCantidadJugadoresEstandar();
            int jugadoresActuales = partido.getJugadoresInscritos() != null ? 
                                   partido.getJugadoresInscritos().size() : 0;
            
            return Math.max(0, jugadoresRequeridos - jugadoresActuales);
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Valida que los niveles mínimo y máximo sean coherentes
     * @param minimo nivel mínimo
     * @param maximo nivel máximo
     * @throws IllegalArgumentException si los niveles son inválidos
     */
    private void validarNiveles(NivelJuego minimo, NivelJuego maximo) {
        if (minimo == null) {
            throw new IllegalArgumentException("El nivel mínimo no puede ser null");
        }
        if (maximo == null) {
            throw new IllegalArgumentException("El nivel máximo no puede ser null");
        }
        if (minimo.ordinal() > maximo.ordinal()) {
            throw new IllegalArgumentException("El nivel mínimo no puede ser mayor que el máximo");
        }
    }
    
    /**
     * Obtiene candidatos filtrados por nivel
     * @param partido el partido de referencia
     * @param candidatos lista de candidatos a filtrar
     * @return lista de candidatos que cumplen con el rango de nivel
     */
    public List<Usuario> filtrarPorNivel(Partido partido, List<Usuario> candidatos) {
        if (partido == null || candidatos == null) {
            return new ArrayList<>();
        }
        
        try {
            return candidatos.stream()
                .filter(candidato -> {
                    try {
                        return esJugadorApto(partido, candidato);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    /**
     * Obtiene estadísticas de los candidatos por nivel
     * @param candidatos lista de candidatos
     * @param deporte deporte a evaluar
     * @return string con estadísticas por nivel
     */
    public String obtenerEstadisticasPorNivel(List<Usuario> candidatos, Deporte deporte) {
        if (candidatos == null || deporte == null) {
            return "Datos insuficientes para generar estadísticas";
        }
        
        try {
            int principiantes = 0;
            int intermedios = 0;
            int avanzados = 0;
            int sinNivel = 0;
            
            for (Usuario candidato : candidatos) {
                try {
                    NivelJuego nivel = candidato.getNivelJuegoParaDeporte(deporte);
                    if (nivel == null) {
                        sinNivel++;
                    } else {
                        switch (nivel) {
                            case PRINCIPIANTE:
                                principiantes++;
                                break;
                            case INTERMEDIO:
                                intermedios++;
                                break;
                            case AVANZADO:
                                avanzados++;
                                break;
                        }
                    }
                } catch (Exception e) {
                    sinNivel++;
                }
            }
            
            StringBuilder stats = new StringBuilder();
            stats.append("=== Estadísticas por Nivel ===\n");
            stats.append("Deporte: ").append(deporte.getNombre()).append("\n");
            stats.append("Total candidatos: ").append(candidatos.size()).append("\n");
            stats.append("Principiantes: ").append(principiantes).append("\n");
            stats.append("Intermedios: ").append(intermedios).append("\n");
            stats.append("Avanzados: ").append(avanzados).append("\n");
            stats.append("Sin nivel definido: ").append(sinNivel).append("\n");
            stats.append("Rango permitido: ").append(nivelMinimo).append(" - ").append(nivelMaximo);
            
            return stats.toString();
        } catch (Exception e) {
            return "Error al generar estadísticas: " + e.getMessage();
        }
    }
    
    // Getters y Setters
    public NivelJuego getNivelMinimo() {
        return nivelMinimo;
    }
    
    public void setNivelMinimo(NivelJuego nivelMinimo) {
        if (nivelMinimo == null) {
            throw new IllegalArgumentException("El nivel mínimo no puede ser null");
        }
        if (nivelMaximo != null && nivelMinimo.ordinal() > nivelMaximo.ordinal()) {
            throw new IllegalArgumentException("El nivel mínimo no puede ser mayor que el máximo");
        }
        this.nivelMinimo = nivelMinimo;
    }
    
    public NivelJuego getNivelMaximo() {
        return nivelMaximo;
    }
    
    public void setNivelMaximo(NivelJuego nivelMaximo) {
        if (nivelMaximo == null) {
            throw new IllegalArgumentException("El nivel máximo no puede ser null");
        }
        if (nivelMinimo != null && nivelMinimo.ordinal() > nivelMaximo.ordinal()) {
            throw new IllegalArgumentException("El nivel mínimo no puede ser mayor que el máximo");
        }
        this.nivelMaximo = nivelMaximo;
    }
    
    /**
     * Configura ambos niveles a la vez
     * @param minimo nivel mínimo
     * @param maximo nivel máximo
     * @throws IllegalArgumentException si los niveles son inválidos
     */
    public void configurarRangoNivel(NivelJuego minimo, NivelJuego maximo) {
        validarNiveles(minimo, maximo);
        this.nivelMinimo = minimo;
        this.nivelMaximo = maximo;
    }
    
    /**
     * Verifica si la estrategia acepta un nivel específico
     * @param nivel el nivel a verificar
     * @return true si está dentro del rango, false en caso contrario
     */
    public boolean aceptaNivel(NivelJuego nivel) {
        if (nivel == null) {
            return false;
        }
        return estaEnRango(nivel, nivelMinimo, nivelMaximo);
    }
    
    /**
     * Obtiene información sobre la configuración actual
     * @return string con información de la configuración
     */
    public String getInformacionConfiguracion() {
        return String.format("EmparejamientoPorNivel{rango: %s - %s}", 
                           nivelMinimo != null ? nivelMinimo.getDescripcion() : "null",
                           nivelMaximo != null ? nivelMaximo.getDescripcion() : "null");
    }
    
    @Override
    public String toString() {
        return String.format("EmparejamientoPorNivel{minimo=%s, maximo=%s}", 
                           nivelMinimo, nivelMaximo);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        EmparejamientoPorNivel that = (EmparejamientoPorNivel) obj;
        return nivelMinimo == that.nivelMinimo && nivelMaximo == that.nivelMaximo;
    }
    
    @Override
    public int hashCode() {
        int result = nivelMinimo != null ? nivelMinimo.hashCode() : 0;
        result = 31 * result + (nivelMaximo != null ? nivelMaximo.hashCode() : 0);
        return result;
    }
}