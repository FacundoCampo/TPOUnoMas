package model.dto;

import enums.NivelJuego;

public class DeporteUsuarioDTO {
    private DeporteDTO deporte;
    private NivelJuego nivel;
    private boolean favorito;

    public DeporteUsuarioDTO(DeporteDTO deporte, NivelJuego nivel, boolean favorito) {
        this.deporte = deporte;
        this.nivel = nivel;
        this.favorito = favorito;
    }

    public DeporteDTO getDeporte() { return deporte; }
    public void setDeporte(DeporteDTO deporte) { this.deporte = deporte; }

    public NivelJuego getNivel() { return nivel; }
    public void setNivel(NivelJuego nivel) { this.nivel = nivel; }

    public boolean esFavorito() { return favorito; }
    public void setFavorito(boolean favorito) { this.favorito = favorito; }
}