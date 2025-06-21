package strategy;

import model.entity.Deporte;
import enums.NivelJuego;
import model.entity.Partido;
import model.entity.Usuario;

import java.util.List;
import java.util.ArrayList;

public class EmparejamientoPorNivel implements IEstrategiaEmparejamiento {

    private NivelJuego nivelMinimo;
    private NivelJuego nivelMaximo;

    public EmparejamientoPorNivel() {
        this.nivelMinimo = NivelJuego.PRINCIPIANTE;
        this.nivelMaximo = NivelJuego.AVANZADO;
    }

    public EmparejamientoPorNivel(NivelJuego nivelMinimo, NivelJuego nivelMaximo) {
        validarNiveles(nivelMinimo, nivelMaximo);
        this.nivelMinimo = nivelMinimo;
        this.nivelMaximo = nivelMaximo;
    }

    @Override
    public List<Usuario> emparejarJugadores(Partido partido, List<Usuario> candidatos) {
        if (partido == null || candidatos == null || partido.getDeporte() == null) {
            throw new IllegalArgumentException("Parámetros inválidos para emparejamiento");
        }

        List<Usuario> candidatosAptos = new ArrayList<>();
        for (Usuario candidato : candidatos) {
            if (esJugadorApto(partido, candidato)) {
                candidatosAptos.add(candidato);
            }
        }

        int jugadoresFaltantes = calcularJugadoresFaltantes(partido);

        List<Usuario> seleccionados = new ArrayList<>();
        for (int i = 0; i < candidatosAptos.size() && seleccionados.size() < jugadoresFaltantes; i++) {
            seleccionados.add(candidatosAptos.get(i));
        }

        return seleccionados;
    }

    @Override
    public boolean esJugadorApto(Partido partido, Usuario jugador) {
        if (partido.getJugadoresInscritos().contains(jugador)) {
            return false;
        }

        NivelJuego nivelJugador = jugador.getNivelJuegoParaDeporte(partido.getDeporte());
        return nivelJugador != null && cumpleRequisitosNivel(jugador, partido.getDeporte());
    }

    public boolean cumpleRequisitosNivel(Usuario usuario, Deporte deporte) {
        if (usuario == null || deporte == null) return false;

        NivelJuego nivelUsuario = usuario.getNivelJuegoParaDeporte(deporte);
        return nivelUsuario != null && estaEnRango(nivelUsuario, nivelMinimo, nivelMaximo);
    }

    private boolean estaEnRango(NivelJuego nivel, NivelJuego minimo, NivelJuego maximo) {
        if (nivel == null || minimo == null || maximo == null) return false;

        return nivel.ordinal() >= minimo.ordinal() && nivel.ordinal() <= maximo.ordinal();
    }

    private int calcularJugadoresFaltantes(Partido partido) {
        if (partido == null || partido.getDeporte() == null) return 0;

        int jugadoresRequeridos = partido.getDeporte().getCantidadJugadoresEstandar();
        int jugadoresActuales = partido.getJugadoresInscritos() != null ? partido.getJugadoresInscritos().size() : 0;
        return Math.max(0, jugadoresRequeridos - jugadoresActuales);
    }

    private void validarNiveles(NivelJuego minimo, NivelJuego maximo) {
        if (minimo == null || maximo == null || minimo.ordinal() > maximo.ordinal()) {
            throw new IllegalArgumentException("Niveles inválidos: mínimo > máximo o null");
        }
    }

    public List<Usuario> filtrarPorNivel(Partido partido, List<Usuario> candidatos) {
        List<Usuario> filtrados = new ArrayList<>();
        if (partido == null || candidatos == null) return filtrados;

        for (Usuario candidato : candidatos) {
            if (esJugadorApto(partido, candidato)) {
                filtrados.add(candidato);
            }
        }
        return filtrados;
    }

    public String obtenerEstadisticasPorNivel(List<Usuario> candidatos, Deporte deporte) {
        if (candidatos == null || deporte == null) {
            return "Datos insuficientes para generar estadísticas";
        }

        int principiantes = 0;
        int intermedios = 0;
        int avanzados = 0;
        int sinNivel = 0;

        for (Usuario candidato : candidatos) {
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
    }

    public NivelJuego getNivelMinimo() {
        return nivelMinimo;
    }

    public void setNivelMinimo(NivelJuego nivelMinimo) {
        if (nivelMinimo == null || (nivelMaximo != null && nivelMinimo.ordinal() > nivelMaximo.ordinal())) {
            throw new IllegalArgumentException("Nivel mínimo inválido");
        }
        this.nivelMinimo = nivelMinimo;
    }

    public NivelJuego getNivelMaximo() {
        return nivelMaximo;
    }

    public void setNivelMaximo(NivelJuego nivelMaximo) {
        if (nivelMaximo == null || (nivelMinimo != null && nivelMinimo.ordinal() > nivelMaximo.ordinal())) {
            throw new IllegalArgumentException("Nivel máximo inválido");
        }
        this.nivelMaximo = nivelMaximo;
    }

    public void configurarRangoNivel(NivelJuego minimo, NivelJuego maximo) {
        validarNiveles(minimo, maximo);
        this.nivelMinimo = minimo;
        this.nivelMaximo = maximo;
    }

    public boolean aceptaNivel(NivelJuego nivel) {
        return nivel != null && estaEnRango(nivel, nivelMinimo, nivelMaximo);
    }

    public String getInformacionConfiguracion() {
        return String.format("EmparejamientoPorNivel{rango: %s - %s}", nivelMinimo, nivelMaximo);
    }

    @Override
    public String toString() {
        return getInformacionConfiguracion();
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