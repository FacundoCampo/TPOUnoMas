package util;

import controller.DeporteController;
import controller.PartidoController;
import model.dto.DeporteDTO;
import model.dto.PartidoDTO;
import model.state.Confirmado;
import model.state.EnJuego;
import model.state.Finalizado;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DataLoader {

    public static void cargarDeportes() {
        DeporteController controller = DeporteController.getInstance();
        controller.crearDeporte(new DeporteDTO("Fútbol", 11));
        controller.crearDeporte(new DeporteDTO("Básquet", 5));
        controller.crearDeporte(new DeporteDTO("Tenis", 1));
        controller.crearDeporte(new DeporteDTO("Paddle", 2));
    }

    public static void cargarPartidos() {
        PartidoController controller = PartidoController.getInstance();
        DeporteController deporteController = DeporteController.getInstance();

        DeporteDTO deporteFutbol = deporteController.buscarPorNombre("Fútbol");
        DeporteDTO deporteTenis = deporteController.buscarPorNombre("Tenis");

        List<PartidoDTO> partidos = new ArrayList<>();

        partidos.add(new PartidoDTO(deporteFutbol, 90, "Cancha 1", obtenerFecha(1)));
        partidos.add(new PartidoDTO(deporteTenis, 60, "Club Tenis", obtenerFecha(1)));
        partidos.add(new PartidoDTO(deporteFutbol, 120, "Estadio", obtenerFecha(1)));
        partidos.add(new PartidoDTO(deporteFutbol, 90, "Cancha 2", obtenerFecha(2)));
        partidos.add(new PartidoDTO(deporteFutbol, 60, "Parque Central", obtenerFecha(2)));
        partidos.add(new PartidoDTO(deporteTenis, 45, "Cancha rápida", obtenerFecha(2)));
        partidos.add(new PartidoDTO(deporteTenis, 90, "Tenis Club A", obtenerFecha(3)));
        partidos.add(new PartidoDTO(deporteFutbol, 75, "Barrio Norte", obtenerFecha(3)));
        partidos.add(new PartidoDTO(deporteFutbol, 90, "Escuela 23", obtenerFecha(3)));
        partidos.add(new PartidoDTO(deporteFutbol, 60, "Estadio Chiquito", obtenerFecha(4)));
        partidos.add(new PartidoDTO(deporteTenis, 120, "Canchas Doradas", obtenerFecha(4)));
        partidos.add(new PartidoDTO(deporteTenis, 80, "Cancha de tierra", obtenerFecha(4)));
        partidos.add(new PartidoDTO(deporteFutbol, 90, "Cancha sur", obtenerFecha(5)));
        partidos.add(new PartidoDTO(deporteTenis, 60, "Tenis Club B", obtenerFecha(5)));
        partidos.add(new PartidoDTO(deporteFutbol, 100, "Cancha Municipal", obtenerFecha(5)));

        for (PartidoDTO dto : partidos) {
            controller.crearPartido(dto);
        }

        List<PartidoDTO> creados = controller.obtenerTodos();
        for (int i = 0; i < creados.size(); i++) {
            if (i % 4 == 0) controller.cambiarEstado(creados.get(i).getId(), new EnJuego());
            else if (i % 4 == 1) controller.cambiarEstado(creados.get(i).getId(), new Confirmado());
            else if (i % 4 == 2) controller.cambiarEstado(creados.get(i).getId(), new Finalizado());
        }
    }

    private static Date obtenerFecha(int diasFuturos) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, diasFuturos);
        return cal.getTime();
    }
}
