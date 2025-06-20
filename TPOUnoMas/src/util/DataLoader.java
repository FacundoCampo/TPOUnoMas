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
        controller.crearDeporte(new DeporteDTO(null, "Fútbol", 11));
        controller.crearDeporte(new DeporteDTO(null, "Básquet", 5));
        controller.crearDeporte(new DeporteDTO(null, "Tenis", 1));
        controller.crearDeporte(new DeporteDTO(null, "Paddle", 2));
    }

    public static void cargarPartidos() {
        PartidoController controller = PartidoController.getInstance();
        DeporteController deporteController = DeporteController.getInstance();

        DeporteDTO deporteFutbol = deporteController.buscarPorNombre("Fútbol");
        DeporteDTO deporteTenis = deporteController.buscarPorNombre("Tenis");

        List<PartidoDTO> partidos = new ArrayList<>();

        partidos.add(new PartidoDTO(deporteFutbol, 90, "Palermo", obtenerFecha(1)));
        partidos.add(new PartidoDTO(deporteTenis, 60, "Caballito", obtenerFecha(1)));
        partidos.add(new PartidoDTO(deporteFutbol, 120, "Recoleta", obtenerFecha(1)));
        partidos.add(new PartidoDTO(deporteFutbol, 90, "Villa Urquiza", obtenerFecha(2)));
        partidos.add(new PartidoDTO(deporteFutbol, 60, "Belgrano", obtenerFecha(2)));
        partidos.add(new PartidoDTO(deporteTenis, 45, "Flores", obtenerFecha(2)));
        partidos.add(new PartidoDTO(deporteTenis, 90, "Almagro", obtenerFecha(3)));
        partidos.add(new PartidoDTO(deporteFutbol, 75, "Barracas", obtenerFecha(3)));
        partidos.add(new PartidoDTO(deporteFutbol, 90, "Boedo", obtenerFecha(3)));
        partidos.add(new PartidoDTO(deporteFutbol, 60, "Chacarita", obtenerFecha(4)));
        partidos.add(new PartidoDTO(deporteTenis, 120, "San Telmo", obtenerFecha(4)));
        partidos.add(new PartidoDTO(deporteTenis, 80, "Parque Chas", obtenerFecha(4)));
        partidos.add(new PartidoDTO(deporteFutbol, 90, "Constitución", obtenerFecha(5)));
        partidos.add(new PartidoDTO(deporteTenis, 60, "Parque Patricios", obtenerFecha(5)));
        partidos.add(new PartidoDTO(deporteFutbol, 100, "Villa Crespo", obtenerFecha(5)));


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
