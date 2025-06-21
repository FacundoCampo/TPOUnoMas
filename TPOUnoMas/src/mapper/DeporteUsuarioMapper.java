package mapper;

import model.Deporte;
import model.DeporteUsuario;
import model.dto.DeporteDTO;
import model.dto.DeporteUsuarioDTO;

public class DeporteUsuarioMapper {

    public static DeporteUsuarioDTO toDTO(DeporteUsuario entity) {
        if (entity == null) return null;
        DeporteDTO deporteDTO = DeporteMapper.toDeporteDTO(entity.getDeporte());
        return new DeporteUsuarioDTO(deporteDTO, entity.getNivelJuego());
    }

    public static DeporteUsuario fromDTO(DeporteUsuarioDTO dto) {
        if (dto == null) return null;
        Deporte deporte = DeporteMapper.fromDeporteDTO(dto.getDeporte());
        return new DeporteUsuario(deporte, dto.getNivel());
    }
}
