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
        controller.crearDeporte(new DeporteDTO(null, "Tenis", 2));
        controller.crearDeporte(new DeporteDTO(null, "Paddle", 4));
    }

    public static void cargarUsuarios() {
        List<DeporteDTO> deportes = DeporteController.getInstance().obtenerTodos();
        Random rand = new Random();

        for (int i = 1; i <= 5; i++) {
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
                    preferenciasDTO.add(new DeporteUsuarioDTO(entry.getKey(), entry.getValue(), false));
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

        DeporteDTO futbol = deporteController.buscarPorNombre("Fútbol");
        DeporteDTO tenis = deporteController.buscarPorNombre("Tenis");

        List<PartidoDTO> partidos = new ArrayList<>();
        partidos.add(new PartidoDTO(futbol, 90, "Palermo", obtenerFecha(1)));
        partidos.add(new PartidoDTO(tenis, 60, "Caballito", obtenerFecha(1)));
        partidos.add(new PartidoDTO(futbol, 120, "Recoleta", obtenerFecha(1)));
        partidos.add(new PartidoDTO(futbol, 90, "Villa Urquiza", obtenerFecha(2)));
        partidos.add(new PartidoDTO(futbol, 60, "Belgrano", obtenerFecha(2)));

        for (PartidoDTO dto : partidos) {
            controller.crearPartido(dto);
        }

        asignarJugadoresAPartidos();
        cambiarEstados();
    }

    private static void asignarJugadoresAPartidos() {
        List<UsuarioDTO> usuarios = UsuarioController.getInstance().obtenerTodos();
        List<PartidoDTO> partidos = PartidoController.getInstance().obtenerTodos();

        Random rand = new Random();
        for (PartidoDTO partido : partidos) {
            int cupo = partido.getDeporte().getCantidadJugadoresEstandar();
            Set<Integer> usados = new HashSet<>();

            while (partido.getJugadoresInscritos().size() < cupo && usados.size() < usuarios.size()) {
                int index = rand.nextInt(usuarios.size());
                if (usados.contains(index)) continue;
                usados.add(index);
                UsuarioDTO usuario = usuarios.get(index);

                try {
                    PartidoController.getInstance().sumarseAlPartido(partido.getId(), usuario.getEmail());
                } catch (Exception ignored) {
                }
            }
        }
    }

    private static void cambiarEstados() {
        List<PartidoDTO> creados = PartidoController.getInstance().obtenerTodos();
        for (int i = 0; i < creados.size(); i++) {
            if (i % 4 == 0) PartidoController.getInstance().cambiarEstado(creados.get(i).getId(), new EnJuego());
            else if (i % 4 == 1) PartidoController.getInstance().cambiarEstado(creados.get(i).getId(), new Confirmado());
            else if (i % 4 == 2) PartidoController.getInstance().cambiarEstado(creados.get(i).getId(), new Finalizado());
        }
    }

    private static Date obtenerFecha(int diasFuturos) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, diasFuturos);
        return cal.getTime();
    }
}
