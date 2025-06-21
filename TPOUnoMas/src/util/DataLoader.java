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
        UsuarioController usuarioController = UsuarioController.getInstance();

        List<UsuarioDTO> usuarios = usuarioController.obtenerTodos();
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados para asignar como organizadores.");
            return;
        }

        DeporteDTO futbol = deporteController.buscarPorNombre("Fútbol");
        DeporteDTO tenis = deporteController.buscarPorNombre("Tenis");

        List<PartidoDTO> partidos = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < 5; i++) {
            DeporteDTO deporte = (i == 1) ? tenis : futbol;
            String ubicacion = switch (i) {
                case 0 -> "Palermo";
                case 1 -> "Caballito";
                case 2 -> "Recoleta";
                case 3 -> "Villa Urquiza";
                default -> "Belgrano";
            };
            Date fecha = obtenerFecha(i < 3 ? 1 : 2);
            String organizadorEmail = usuarios.get(rand.nextInt(usuarios.size())).getEmail();

            PartidoDTO dto = new PartidoDTO(deporte, 90, ubicacion, fecha, organizadorEmail);
            String partidoId = controller.crearPartido(dto);

            try {
                controller.sumarseAlPartido(partidoId, organizadorEmail);
            } catch (Exception e) {
                System.out.println("No se pudo inscribir al organizador: " + organizadorEmail);
            }
        }

        asignarJugadoresAPartidos();
        cambiarEstados();
    }


    private static void asignarJugadoresAPartidos() {
        List<UsuarioDTO> usuarios = UsuarioController.getInstance().obtenerTodos();
        List<PartidoDTO> partidos = PartidoController.getInstance().obtenerTodos();

        Random rand = new Random();
        boolean partidoConUnLugar = false;

        for (int i = 0; i < partidos.size(); i++) {
            PartidoDTO partido = partidos.get(i);
            int cupo = partido.getDeporte().getCantidadJugadoresEstandar();
            Set<Integer> usados = new HashSet<>();

            int cantidadAInscribir = cupo;

            if (!partidoConUnLugar) {
                cantidadAInscribir = cupo - 1;
                partidoConUnLugar = true;
            }

            while (partido.getJugadoresInscritos().size() < cantidadAInscribir && usados.size() < usuarios.size()) {
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
            PartidoDTO partido = creados.get(i);
            int inscritos = partido.getJugadoresInscritos().size();
            int necesarios = partido.getDeporte().getCantidadJugadoresEstandar();

            if (i % 4 == 0) {
                if (inscritos >= necesarios) {
                    PartidoController.getInstance().cambiarEstado(partido.getId(), new EnJuego());
                }
            } else if (i % 4 == 1) {
                if (inscritos >= necesarios) {
                    PartidoController.getInstance().cambiarEstado(partido.getId(), new Confirmado());
                }
            } else if (i % 4 == 2) {
                if (inscritos >= necesarios) {
                    PartidoController.getInstance().cambiarEstado(partido.getId(), new Finalizado());
                }
            }
        }
    }


    private static Date obtenerFecha(int diasFuturos) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, diasFuturos);
        return cal.getTime();
    }
}
