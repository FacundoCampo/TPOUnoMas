package mapper;

import enums.NivelJuego;
import model.Deporte;
import model.DeporteUsuario;
import model.Usuario;
import model.dto.DeporteDTO;
import model.dto.DeporteUsuarioDTO;
import model.dto.UsuarioDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UsuarioMapper {

    public static UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) return null;

        UsuarioDTO dto = new UsuarioDTO(
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getContraseña(),
                usuario.getUbicacion()
        );

        List<DeporteUsuarioDTO> preferencias = new ArrayList<>();
        List<DeporteUsuario> deportesUsuario = usuario.getDeportesUsuario();

        if (deportesUsuario != null) {
            for (int i = 0; i < deportesUsuario.size(); i++) {
                DeporteUsuario du = deportesUsuario.get(i);
                Deporte d = du.getDeporte();
                DeporteDTO deporteDTO = DeporteMapper.toDeporteDTO(d);
                NivelJuego nivel = du.getNivelJuego();
                preferencias.add(new DeporteUsuarioDTO(deporteDTO, nivel, du.esFavorito()));
            }
        }

        dto.setPreferenciasDeportivas(preferencias);

        return dto;
    }


    public static Usuario fromDto(UsuarioDTO dto) {
        if (dto == null) return null;

        Usuario usuario = new Usuario(
                dto.getNombre(),
                dto.getEmail(),
                dto.getContrasena(),
                dto.getUbicacion()
        );

        List<DeporteUsuario> preferencias = dto.getPreferenciasDeportivas().stream()
                .map(dudto -> new DeporteUsuario(
                        new model.Deporte(dudto.getDeporte().getId(), dudto.getDeporte().getCantidadJugadoresEstandar()),
                        dudto.getNivel()
                )).collect(Collectors.toList());

        usuario.setDeportesUsuario(preferencias);

        return usuario;
    }
}
