package model.dto;

import enums.NivelJuego;

public class DeporteUsuarioDTO {
    private DeporteDTO deporte;
    private NivelJuego nivel;

    public DeporteUsuarioDTO(DeporteDTO deporte, NivelJuego nivel) {
        this.deporte = deporte;
        this.nivel = nivel;
    }

    public DeporteDTO getDeporte() { return deporte; }
    public void setDeporte(DeporteDTO deporte) { this.deporte = deporte; }

    public NivelJuego getNivel() { return nivel; }
    public void setNivel(NivelJuego nivel) { this.nivel = nivel; }
}