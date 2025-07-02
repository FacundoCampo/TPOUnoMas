package model.dto;

import enums.EstadoPartido;
import enums.TipoEmparejamiento;
import model.estadosDelPartido.IEstadoPartido;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PartidoDTO {
    private String id;
    private DeporteDTO deporte;
    private int duracion;
    private String ubicacion;
    private Date fechaHora;
    private List<UsuarioDTO> jugadoresInscritos;
    private EstadoPartido estado;
    private String organizador;
    private TipoEmparejamiento tipoEmparejamiento;

    public PartidoDTO(DeporteDTO deporte, int duracion, String ubicacion, Date fechaHora, String organizador, TipoEmparejamiento tipoEmparejamiento) {
        super();
        this.deporte = deporte;
        this.duracion = duracion;
        this.ubicacion = ubicacion;
        this.fechaHora = fechaHora != null ? new Date(fechaHora.getTime()) : null;
        this.jugadoresInscritos = new ArrayList<>();
        this.organizador = organizador;
        this.tipoEmparejamiento = tipoEmparejamiento;
    }

    public String getId() { return id; }
    public String getOrganizador() { return organizador; }
    public DeporteDTO getDeporte() { return deporte; }
    public int getDuracion() { return duracion; }
    public String getUbicacion() { return ubicacion; }
    public Date getFechaHora() { return fechaHora; }
    public EstadoPartido getEstado() { return this.estado; }
    public List<UsuarioDTO> getJugadoresInscritos() { return new ArrayList<>(jugadoresInscritos); }
    public TipoEmparejamiento getTipoEmparejamiento() {
        return tipoEmparejamiento;
    }

    public void setDeporte(DeporteDTO deporte) { this.deporte = deporte; }
    public void setId(String id) { this.id = id; }
    public void setEstado(EstadoPartido estado) { this.estado = estado; }
    public void setJugadoresInscritos(List<UsuarioDTO> jugadoresInscritos) {
        this.jugadoresInscritos = jugadoresInscritos != null ? new ArrayList<>(jugadoresInscritos) : new ArrayList<>();
    }

    public boolean esValido() {
        return deporte != null
                && deporte.getId() != null && !deporte.getId().isBlank()
                && duracion > 0
                && ubicacion != null && !ubicacion.isBlank()
                && fechaHora != null;
    }

}
