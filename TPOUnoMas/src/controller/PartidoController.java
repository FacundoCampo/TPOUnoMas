package controller;

import enums.TipoEmparejamiento;
import model.dto.PartidoDTO;
import model.dto.UsuarioDTO;
import model.estadosDelPartido.IEstadoPartido;
import services.PartidoService;
import services.interfaces.IPartidoService;

import java.util.List;

public class PartidoController {
    private static PartidoController instance = null;

    private final IPartidoService partidoService;

    public static PartidoController getInstance() {
        if (instance == null) {
            instance = new PartidoController();
        }
        return instance;
    }

    private PartidoController() {
        this.partidoService = new PartidoService();
    }

    public String crearPartido(PartidoDTO dto) {
        if (dto == null || !dto.esValido()) {
            throw new IllegalArgumentException("El partido no es válido");
        }
        return partidoService.crearPartido(dto);
    }

    public List<PartidoDTO> obtenerTodos() {
        return partidoService.obtenerTodos();
    }

    public List<PartidoDTO> obtenerPartidosDelUsuario(String usuarioid) {
        return partidoService.obtenerPartidosDelUsuario(usuarioid);
    }

    public List<PartidoDTO> obtenerHistorial(String usuarioid) {
        return partidoService.obtenerHistorial(usuarioid);
    }

    public boolean cambiarEstado(String idPartido, IEstadoPartido estado) {
        return partidoService.cambiarEstado(idPartido, estado);
    }

    public boolean sumarseAlPartido(String partidoid, String usuarioid) {
        return partidoService.sumarseAlPartido(partidoid, usuarioid);
    }

    public List<UsuarioDTO> emparejar(String partidoId) {
        return partidoService.emparejar(partidoId);
    }
    public boolean cambiarTipoEmparejamiento(String id, TipoEmparejamiento nuevoTipo) {
        return partidoService.cambiarTipoEmparejamiento(id, nuevoTipo);
    }

}

