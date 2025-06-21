package model.entity;

import java.util.List;

public class ResultadoEmparejamiento {
    private final List<Usuario> jugadoresSeleccionados;
    private final int totalCandidatos;
    private final long tiempoEjecucion;
    private final String estrategiaUtilizada;
    private final boolean exitoso;
    private final String mensajeError;

    public ResultadoEmparejamiento(List<Usuario> jugadoresSeleccionados,
                                   int totalCandidatos,
                                   long tiempoEjecucion,
                                   String estrategiaUtilizada,
                                   boolean exitoso,
                                   String mensajeError) {
        this.jugadoresSeleccionados = jugadoresSeleccionados;
        this.totalCandidatos = totalCandidatos;
        this.tiempoEjecucion = tiempoEjecucion;
        this.estrategiaUtilizada = estrategiaUtilizada;
        this.exitoso = exitoso;
        this.mensajeError = mensajeError;
    }

    public List<Usuario> getJugadoresSeleccionados() { return jugadoresSeleccionados; }
    public int getTotalCandidatos() { return totalCandidatos; }
    public long getTiempoEjecucion() { return tiempoEjecucion; }
    public String getEstrategiaUtilizada() { return estrategiaUtilizada; }
    public boolean esExitoso() { return exitoso; }
    public String getMensajeError() { return mensajeError; }

    public int getCantidadSeleccionados() {
        return jugadoresSeleccionados != null ? jugadoresSeleccionados.size() : 0;
    }

    public double getTasaSeleccion() {
        if (totalCandidatos == 0) return 0.0;
        return (getCantidadSeleccionados() * 100.0) / totalCandidatos;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Resultado de Emparejamiento ===\n");
        sb.append("Exitoso: ").append(exitoso ? "Sí" : "No").append("\n");

        if (exitoso) {
            sb.append("Estrategia: ").append(estrategiaUtilizada).append("\n");
            sb.append("Candidatos evaluados: ").append(totalCandidatos).append("\n");
            sb.append("Jugadores seleccionados: ").append(getCantidadSeleccionados()).append("\n");
            sb.append("Tasa de selección: ").append(String.format("%.1f%%", getTasaSeleccion())).append("\n");
            sb.append("Tiempo de ejecución: ").append(tiempoEjecucion).append(" ms\n");
        } else {
            sb.append("Error: ").append(mensajeError).append("\n");
        }

        return sb.toString();
    }
}