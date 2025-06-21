package mapper;

import model.entity.Deporte;
import model.entity.DeporteUsuario;
import model.dto.DeporteDTO;
import model.dto.DeporteUsuarioDTO;

public class DeporteUsuarioMapper {

    public static DeporteUsuarioDTO toDTO(DeporteUsuario entity) {
        if (entity == null) return null;
        DeporteDTO deporteDTO = DeporteMapper.toDeporteDTO(entity.getDeporte());
        return new DeporteUsuarioDTO(deporteDTO, entity.getNivelJuego(), entity.esFavorito());
    }

    public static DeporteUsuario fromDTO(DeporteUsuarioDTO dto) {
        if (dto == null) return null;
        Deporte deporte = DeporteMapper.fromDeporteDTO(dto.getDeporte());
        return new DeporteUsuario(deporte, dto.getNivel(), dto.esFavorito());
    }
}
