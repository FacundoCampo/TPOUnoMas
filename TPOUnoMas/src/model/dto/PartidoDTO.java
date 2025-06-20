package model.dto;

import model.Notificacion;
import model.state.IEstadoPartido;

import java.util.Date;
import java.util.List;

public class PartidoDTO {
    private String id;
    private DeporteDTO deporte;
    private int duracion;
    private String ubicacion;
    private Date fechaHora;
    private List<UsuarioDTO> jugadoresInscritos;
    private IEstadoPartido estado;
    private List<Notificacion> notificaciones;

    public PartidoDTO() {
        super();
    }

    public PartidoDTO(DeporteDTO deporte, int duracion, String ubicacion, Date fechaHora) {
        super();
        this.deporte = deporte;
        this.duracion = duracion;
        this.ubicacion = ubicacion;
        this.fechaHora = fechaHora != null ? new Date(fechaHora.getTime()) : null;
    }

    public String getId() { return id; }
    public DeporteDTO getDeporte() { return deporte; }
    public int getDuracion() { return duracion; }
    public String getUbicacion() { return ubicacion; }
    public Date getFechaHora() { return fechaHora; }
    public IEstadoPartido getEstado() { return this.estado; }

    public void setDeporte(DeporteDTO deporte) { this.deporte = deporte; }
    public void setId(String id) { this.id = id; }
    public void setEstado(IEstadoPartido estado) { this.estado = estado; }

    public boolean esValido() {
        return deporte != null
                && deporte.getId() != null && !deporte.getId().isBlank()
                && duracion > 0
                && ubicacion != null && !ubicacion.isBlank()
                && fechaHora != null;
    }

}
