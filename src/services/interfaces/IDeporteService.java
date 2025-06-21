package services.interfaces;

import model.dto.DeporteDTO;

import java.util.List;

public interface IDeporteService {
    List<DeporteDTO> obtenerTodos();
    DeporteDTO buscarPorNombre(String nombre);
    boolean crearDeporte(DeporteDTO dto);
    boolean modificarDeporte(String nombreViejo, DeporteDTO datosNuevos);
    boolean eliminarPorNombre(String nombre);
    List<String> obtenerNombresDisponibles();
}
