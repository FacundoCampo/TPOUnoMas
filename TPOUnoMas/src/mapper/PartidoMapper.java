package mapper;

import model.dto.UsuarioDTO;
import model.entity.Deporte;
import model.entity.Partido;
import model.dto.DeporteDTO;
import model.dto.PartidoDTO;
import model.entity.Usuario;

import java.util.ArrayList;
import java.util.List;

public class PartidoMapper {
    private static DeporteMapper dm;

    public static PartidoDTO toDTO(Partido partido) {
        if (partido == null) return null;

        DeporteDTO deporteDTO = dm.toDeporteDTO(partido.getDeporte());
        PartidoDTO dto = new PartidoDTO(
                deporteDTO,
                partido.getDuracion(),
                partido.getUbicacion(),
                partido.getFechaHora(),
                partido.getOrganizadorID()
        );
        dto.setId(partido.getId());
        dto.setEstado(partido.getEstado());

        List<UsuarioDTO> jugadoresDTO = new ArrayList<>();
        for (Usuario jugador : partido.getJugadoresInscritos()) {
            jugadoresDTO.add(new UsuarioDTO(
                    jugador.getNombre(),
                    jugador.getEmail(),
                    null,
                    jugador.getUbicacion()
            ));
        }
        dto.setJugadoresInscritos(jugadoresDTO);
        return dto;
    }

    public static Partido fromDTO(PartidoDTO dto) {
        if (dto == null) return null;

        Deporte deporte = dm.fromDeporteDTO(dto.getDeporte());
        Partido partido = new Partido(
                deporte,
                dto.getDuracion(),
                dto.getUbicacion(),
                dto.getFechaHora(),
                dto.getOrganizador()
        );
        partido.setId(dto.getId());
        if(dto.getEstado() != null) partido.setEstado(dto.getEstado());
        return partido;
    }
}
