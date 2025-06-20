package controller;

import model.dto.DeporteDTO;
import services.DeporteService;

import java.util.List;

public class DeporteController {
    private static DeporteController instance;
    private final DeporteService servicio;

    private DeporteController() {
        this.servicio = new DeporteService();
    }

    public static DeporteController getInstance() {
        if (instance == null) {
            instance = new DeporteController();
        }
        return instance;
    }

    public List<DeporteDTO> obtenerTodos() {
        return servicio.obtenerTodos();
    }

    public DeporteDTO buscarPorNombre(String nombre) {
        return servicio.buscarPorNombre(nombre);
    }

    public void crearDeporte(DeporteDTO dto) {
        servicio.crearDeporte(dto);
    }

    public boolean modificarDeporte(String nombreViejo, DeporteDTO datosNuevos) {
        return servicio.modificarDeporte(nombreViejo, datosNuevos);
    }

}
