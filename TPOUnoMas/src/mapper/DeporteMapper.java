package mapper;

import model.Deporte;
import model.dto.DeporteDTO;

public class DeporteMapper {
    public static DeporteDTO toDeporteDTO(Deporte deporte) {
        if (deporte == null) return null;
        return new DeporteDTO(
                deporte.getId(),
                deporte.getNombre(),
                deporte.getCantidadJugadoresEstandar()
        );
    }

    public static Deporte fromDeporteDTO(DeporteDTO dto) {
        if (dto == null) return null;
        return new Deporte(
                dto.getId(),
                dto.getNombre(),
                dto.getCantidadJugadoresEstandar()
        );
    }
}
