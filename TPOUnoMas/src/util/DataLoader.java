package util;

import controller.DeporteController;
import controller.PartidoController;
import controller.UsuarioController;
import enums.NivelJuego;
import model.dto.DeporteDTO;
import model.dto.DeporteUsuarioDTO;
import model.dto.PartidoDTO;
import model.dto.UsuarioDTO;
import model.estadosDelPartido.Confirmado;
import model.estadosDelPartido.EnJuego;
import model.estadosDelPartido.Finalizado;

import java.util.*;

public class DataLoader {

    public static void cargarDeportes() {
        DeporteController controller = DeporteController.getInstance();
        controller.crearDeporte(new DeporteDTO(null, "Fútbol", 11));
        controller.crearDeporte(new DeporteDTO(null, "Básquet", 5));
        controller.crearDeporte(new DeporteDTO(null, "Tenis", 1));
        controller.crearDeporte(new DeporteDTO(null, "Paddle", 2));
    }

    public static void cargarUsuarios() {
        List<DeporteDTO> deportes = new ArrayList<>();
        List<DeporteDTO> originales = DeporteController.getInstance().obtenerTodos();

        for (int i = 0; i < originales.size(); i++) {
            deportes.add(originales.get(i));
        }

        Random rand = new Random();

        for (int i = 1; i <= 20; i++) {
            String nombre = "Usuario" + i;
            String email = "usuario" + i + "@mail.com";
            String contrasena = "clave" + i;
            String ubicacion = "Ciudad" + ((i % 5) + 1);

            UsuarioDTO usuario = new UsuarioDTO(nombre, email, contrasena, ubicacion);

            Map<DeporteDTO, NivelJuego> preferencias = new HashMap<>();

            int cantidadPreferencias = rand.nextInt(3) + 1;
            List<Integer> usados = new ArrayList<>();

            while (preferencias.size() < cantidadPreferencias && deportes.size() > 0) {
                int indice = rand.nextInt(deportes.size());
                if (!usados.contains(indice)) {
                    usados.add(indice);
                    DeporteDTO deporte = deportes.get(indice);
                    NivelJuego nivel = NivelJuego.values()[rand.nextInt(NivelJuego.values().length)];
                    preferencias.put(deporte, nivel);
                }
            }

            try {
                UsuarioController.getInstance().registrar(usuario);

                List<DeporteUsuarioDTO> preferenciasDTO = new ArrayList<>();

                for (Map.Entry<DeporteDTO, NivelJuego> entry : preferencias.entrySet()) {
                    preferenciasDTO.add(new DeporteUsuarioDTO(entry.getKey(), entry.getValue()));
                }

                usuario.setPreferenciasDeportivas(preferenciasDTO);
            } catch (IllegalArgumentException e) {
                System.out.println("Usuario duplicado: " + email);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
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
