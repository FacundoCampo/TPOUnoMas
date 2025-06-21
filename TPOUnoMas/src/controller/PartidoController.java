package controller;

import model.dto.PartidoDTO;
import model.estadosDelPartido.IEstadoPartido;
import services.PartidoService;
import services.interfaces.IPartidoService;
import strategy.EmparejamientoContext;
import strategy.IEstrategiaEmparejamiento;

import java.util.List;

public class PartidoController {
    private static PartidoController instance = null;

    private final IPartidoService partidoService;
    private final EmparejamientoContext emparejamientoService;

    public static PartidoController getInstance() {
        if (instance == null) {
            instance = new PartidoController();
        }
        return instance;
    }

    private PartidoController() {
        this.partidoService = new PartidoService();
        this.emparejamientoService = new EmparejamientoContext();
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

    public boolean cambiarEstado(String idPartido, IEstadoPartido estado) {
        return partidoService.cambiarEstado(idPartido, estado);
    }

    public boolean sumarseAlPartido(String partidoid, String usuarioid) {
        return partidoService.sumarseAlPartido(partidoid, usuarioid);
    }

}

