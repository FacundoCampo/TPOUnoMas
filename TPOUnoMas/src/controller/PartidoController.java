package controller;

import model.dto.PartidoDTO;
import model.state.IEstadoPartido;
import services.PartidoService;
import strategy.EmparejamientoContext;
import strategy.IEstrategiaEmparejamiento;

import java.util.List;
import java.util.Optional;

public class PartidoController {
    private static PartidoController instance = null;

    private final PartidoService partidoService;
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

    public void crearPartido(PartidoDTO dto) {
        if (dto == null || !dto.esValido()) {
            throw new IllegalArgumentException("El partido no es válido");
        }
        partidoService.crearPartido(dto);
    }

    public void crearPartido(PartidoDTO dto, IEstrategiaEmparejamiento estrategia) { //es necesaria la estrategia al crearlo?
        if (dto == null || !dto.esValido()) {
            throw new IllegalArgumentException("El partido no es válido");
        }
        partidoService.crearPartido(dto);
    }

    public List<PartidoDTO> obtenerTodos() {
        return partidoService.obtenerTodos();
    }

    public boolean cambiarEstado(String idPartido, IEstadoPartido estado) {
        return partidoService.cambiarEstado(idPartido, estado);
    }

    public boolean sumarseAlPartido(String partidoid, String usuarioid) {
        return true;
    }
}

