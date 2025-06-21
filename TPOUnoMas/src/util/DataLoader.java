package util;

import controller.DeporteController;
import controller.PartidoController;
import controller.UsuarioController;
import enums.NivelJuego;
import enums.TipoEmparejamiento;
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
        String[] ubicaciones = {"Palermo", "Caballito", "Recoleta", "Villa Urquiza", "Belgrano", "Almagro"};

        for (int i = 1; i <= 20; i++) {
            String nombre = "Usuario" + i;
            String email = "usuario" + i + "@mail.com";
            String contrasena = "clave" + i;

            // Solo usuario11 debe tener ubicación fija en Almagro
            String ubicacion = email.equalsIgnoreCase("usuario11@mail.com")
                    ? "Almagro"
                    : ubicaciones[i % ubicaciones.length];

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
        DeporteDTO futbol = deporteController.buscarPorNombre("Fútbol");
        DeporteDTO tenis = deporteController.buscarPorNombre("Tenis");
        DeporteDTO paddle = deporteController.buscarPorNombre("Paddle");
        DeporteDTO basquet = deporteController.buscarPorNombre("Básquet");

        TipoEmparejamiento[] estrategias = TipoEmparejamiento.values();
        String[] ubicaciones = {"Palermo", "Caballito", "Recoleta", "Villa Urquiza", "Belgrano", "Almagro"};

        int partidoIndex = 0;
        for (UsuarioDTO organizador : usuarios) {
            for (int i = 0; i < 2; i++) { // 2 partidos por usuario
                DeporteDTO deporte = switch ((partidoIndex++) % 4) {
                    case 0 -> futbol;
                    case 1 -> tenis;
                    case 2 -> paddle;
                    default -> basquet;
                };

                String ubicacion = ubicaciones[partidoIndex % ubicaciones.length];
                Date fecha = obtenerFecha(i + 1);
                TipoEmparejamiento tipo = estrategias[partidoIndex % estrategias.length];

                PartidoDTO dto = new PartidoDTO(deporte, 90, ubicacion, fecha, organizador.getEmail(), tipo);
                String partidoId = controller.crearPartido(dto);

                try {
                    controller.sumarseAlPartido(partidoId, organizador.getEmail());
                } catch (Exception e) {
                    System.out.println("No se pudo inscribir al organizador: " + organizador.getEmail());
                }
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

            if (i % 4 == 0 && inscritos >= necesarios) {
                PartidoController.getInstance().cambiarEstado(partido.getId(), new EnJuego());
            } else if (i % 4 == 1 && inscritos >= necesarios) {
                PartidoController.getInstance().cambiarEstado(partido.getId(), new Confirmado());
            } else if (i % 4 == 2 && inscritos >= necesarios) {
                PartidoController.getInstance().cambiarEstado(partido.getId(), new Finalizado());
            }
        }
    }

    public static void cargarPartidosParaUsuario1() {
        PartidoController partidoController = PartidoController.getInstance();
        UsuarioController usuarioController = UsuarioController.getInstance();
        DeporteController deporteController = DeporteController.getInstance();

        UsuarioDTO usuario1 = usuarioController.buscarPorId("usuario1@mail.com");
        if (usuario1 == null) {
            System.out.println("Usuario1@mail.com no encontrado.");
            return;
        }

        List<DeporteDTO> deportes = deporteController.obtenerTodos();
        if (deportes.isEmpty()) return;

        TipoEmparejamiento[] estrategias = TipoEmparejamiento.values();
        String[] ubicaciones = {"Palermo", "Belgrano", "Caballito", "Recoleta", "Almagro"};

        for (int i = 0; i < 5; i++) {
            DeporteDTO deporte = deportes.get(i % deportes.size());
            String ubicacion = ubicaciones[i % ubicaciones.length];
            Date fecha = obtenerFecha(i + 2);
            TipoEmparejamiento estrategia = estrategias[i % estrategias.length];

            PartidoDTO dto = new PartidoDTO(deporte, 60, ubicacion, fecha, usuario1.getEmail(), estrategia);
            String partidoId = partidoController.crearPartido(dto);

            try {
                partidoController.sumarseAlPartido(partidoId, usuario1.getEmail());
            } catch (Exception e) {
                System.out.println("No se pudo inscribir al organizador: " + usuario1.getEmail());
            }

            // Simular inscripción de jugadores para alcanzar cupo según el estado
            int cupo = deporte.getCantidadJugadoresEstandar();
            List<UsuarioDTO> otrosUsuarios = usuarioController.obtenerTodos();
            int cantidadAInscribir = switch (i) {
                case 0 -> cupo;       // Confirmado
                case 1 -> cupo;       // EnJuego
                case 2 -> cupo;       // Finalizado
                case 3 -> cupo - 1;   // NecesitamosJugadores (por defecto)
                case 4 -> cupo / 2;   // Incompleto (para mostrar variedad)
                default -> 0;
            };

            int inscriptos = 0;
            for (UsuarioDTO u : otrosUsuarios) {
                if (inscriptos >= cantidadAInscribir || u.getEmail().equalsIgnoreCase(usuario1.getEmail())) continue;
                try {
                    partidoController.sumarseAlPartido(partidoId, u.getEmail());
                    inscriptos++;
                } catch (Exception ignored) {}
            }

            if (i == 0) {
                partidoController.cambiarEstado(partidoId, new Confirmado());
            } else if (i == 1) {
                partidoController.cambiarEstado(partidoId, new EnJuego());
            } else if (i == 2) {
                partidoController.cambiarEstado(partidoId, new Finalizado());
            }
        }
    }

    public static void asignarTenisAUsuario8() {
        UsuarioController usuarioController = UsuarioController.getInstance();
        DeporteController deporteController = DeporteController.getInstance();

        List<DeporteUsuarioDTO> preferencias = usuarioController.obtenerPreferencias("usuario8@mail.com");

        DeporteDTO f = deporteController.buscarPorNombre("Fútbol");

        boolean yaTieneTenis = preferencias.stream()
                .anyMatch(p -> p.getDeporte().getNombre().equalsIgnoreCase("Fútbol"));

        if (!yaTieneTenis) {
            preferencias.add(new DeporteUsuarioDTO(f, NivelJuego.INTERMEDIO, false));
            usuarioController.actualizarPreferencias("usuario8@mail.com", preferencias);
        } else {
            System.out.println("usuario8@mail.com ya tenía Tenis en sus preferencias.");
        }
    }


    private static Date obtenerFecha(int diasFuturos) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, diasFuturos);
        return cal.getTime();
    }
}
