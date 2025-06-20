package services;

import model.Deporte;
import model.dto.DeporteDTO;
import repository.DeporteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DeporteService {

    private static DeporteService instance;
    private final DeporteRepository repository;

    public DeporteService() {
        this.repository = new DeporteRepository();
    }

    public static DeporteService getInstance() {
        if (instance == null) {
            instance = new DeporteService();
        }
        return instance;
    }

    public List<DeporteDTO> obtenerTodos() {
        List<Deporte> lista = repository.obtenerTodos();
        List<DeporteDTO> resultado = new ArrayList<>();
        for (Deporte d : lista) {
            resultado.add(new DeporteDTO(d.getId(), d.getNombre(), d.getCantidadJugadoresEstandar()));
        }
        return resultado;
    }

    public DeporteDTO buscarPorNombre(String nombre) {
        Optional<Deporte> opt = repository.buscarPorNombre(nombre);
        return opt.map(d -> new DeporteDTO(d.getId(), d.getNombre(), d.getCantidadJugadoresEstandar())).orElse(null);
    }

    public boolean crearDeporte(DeporteDTO dto) {
        if (dto == null || dto.getNombre() == null || dto.getNombre().isBlank() || dto.getCantidadJugadoresEstandar() <= 0) {
            return false;
        }

        if (repository.buscarPorNombre(dto.getNombre()).isPresent()) {
            return false;
        }

        Deporte nuevo = new Deporte(UUID.randomUUID().toString(), dto.getNombre().trim(), dto.getCantidadJugadoresEstandar());
        repository.guardar(nuevo);
        return true;
    }

    public boolean modificarDeporte(String nombreViejo, DeporteDTO datosNuevos) {
        Optional<Deporte> existente = repository.buscarPorNombre(nombreViejo);
        if (existente.isEmpty() || datosNuevos == null || datosNuevos.getCantidadJugadoresEstandar() <= 0) {
            return false;
        }

        Deporte deporte = existente.get();
        deporte.setNombre(datosNuevos.getNombre());
        deporte.setCantidadJugadoresEstandar(datosNuevos.getCantidadJugadoresEstandar());
        return repository.actualizar(deporte);
    }

    public boolean eliminarPorNombre(String nombre) {
        return repository.eliminarPorNombre(nombre);
    }

    public List<String> obtenerNombresDisponibles() {
        return repository.obtenerNombresDisponibles();
    }
}
