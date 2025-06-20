package mapper;

import model.Deporte;
import model.Partido;
import model.dto.DeporteDTO;
import model.dto.PartidoDTO;

public class PartidoMapper {
    private static DeporteMapper dm;

    public static PartidoDTO toDTO(Partido partido) {
        if (partido == null) return null;

        DeporteDTO deporteDTO = dm.toDeporteDTO(partido.getDeporte());
        PartidoDTO dto = new PartidoDTO(
                deporteDTO,
                partido.getDuracion(),
                partido.getUbicacion(),
                partido.getFechaHora()
        );
        dto.setId(partido.getId());
        dto.setEstado(partido.getEstado());
        return dto;
    }

    public static Partido fromDTO(PartidoDTO dto) {
        if (dto == null) return null;

        Deporte deporte = dm.fromDeporteDTO(dto.getDeporte());
        Partido partido = new Partido(
                deporte,
                dto.getDuracion(),
                dto.getUbicacion(),
                dto.getFechaHora()
        );
        partido.setId(dto.getId());
        if(dto.getEstado() != null) partido.setEstado(dto.getEstado());
        return partido;
    }
}
