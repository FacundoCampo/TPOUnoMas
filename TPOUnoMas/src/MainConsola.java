import controller.DeporteController;
import controller.PartidoController;
import model.dto.DeporteDTO;
import model.dto.PartidoDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.DataLoader;

public class MainConsola {
    private static final Scanner scanner = new Scanner(System.in);
    private static final PartidoController partidoController = PartidoController.getInstance();
    private static final DeporteController deporteController = DeporteController.getInstance();

    public static void main(String[] args) {
        DataLoader.cargarDeportes();
        DataLoader.cargarPartidos();

        while (true) {
            System.out.println("\n=== MENÚ DE PARTIDOS ===");
            System.out.println("1. Crear partido");
            System.out.println("2. Listar partidos");
            System.out.println("3. Filtrar por estado");
            System.out.println("4. Sumarse a partido");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            String input = scanner.nextLine();
            if (input.equals("0")) System.exit(0);
            int opcion = Integer.parseInt(input);

            switch (opcion) {
                case 1 -> crearPartido();
                case 2 -> listarPartidos();
                case 3 -> filtrarPorEstado();
                case 4 -> sumarseAPartido();
                default -> System.out.println("Opción inválida");
            }
        }
    }

    private static void crearPartido() {
        System.out.println("\n=== CREAR PARTIDO ===");

        List<DeporteDTO> deportes = deporteController.obtenerTodos();
        if (deportes.isEmpty()) {
            System.out.println("No hay deportes disponibles.");
            return;
        }

        for (int i = 0; i < deportes.size(); i++) {
            System.out.println((i + 1) + ". " + deportes.get(i).getNombre());
        }
        System.out.print("Seleccione el deporte (0 para cancelar): ");
        String input = scanner.nextLine();
        if (input.equals("0")) return;
        int deporteIndex = Integer.parseInt(input) - 1;
        DeporteDTO deporte = deportes.get(deporteIndex);

        System.out.print("Duración en minutos (0 para cancelar): ");
        input = scanner.nextLine();
        if (input.equals("0")) return;
        int duracion = Integer.parseInt(input);

        System.out.print("Ubicación (0 para cancelar): ");
        String ubicacion = scanner.nextLine();
        if (ubicacion.equals("0")) return;

        System.out.print("Fecha y hora (dd/MM/yyyy HH:mm) (0 para cancelar): ");
        String fechaStr = scanner.nextLine();
        if (fechaStr.equals("0")) return;
        Date fecha = parsearAFecha(fechaStr);

        PartidoDTO partido = new PartidoDTO(deporte, duracion, ubicacion, fecha);
        partidoController.crearPartido(partido);
        System.out.println("Partido creado correctamente.");
    }

    private static void listarPartidos() {
        System.out.println("\n=== LISTA DE PARTIDOS ===");
        List<PartidoDTO> partidos = partidoController.obtenerTodos();
        for (PartidoDTO p : partidos) {
            mostrarPartido(p);
        }
    }

    private static void filtrarPorEstado() {
        System.out.print("Ingrese nombre del estado (Ej: Necesitamos jugadores, Confirmado, En juego) (0 para cancelar): ");
        String estado = scanner.nextLine();
        if (estado.equals("0")) return;

        List<PartidoDTO> partidos = partidoController.obtenerTodos().stream()
                .filter(p -> p.getEstado().getNombre().equalsIgnoreCase(estado))
                .toList();

        if (partidos.isEmpty()) {
            System.out.println("No se encontraron partidos en ese estado.");
        } else {
            partidos.forEach(MainConsola::mostrarPartido);
        }
    }

    private static void sumarseAPartido() {
        System.out.print("Ingrese ID del partido (0 para cancelar): ");
        String id = scanner.nextLine();
        if (id.equals("0")) return;

        System.out.print("Ingrese su ID de usuario (0 para cancelar): ");
        String usuarioId = scanner.nextLine();
        if (usuarioId.equals("0")) return;

        boolean ok = partidoController.sumarseAlPartido(id, usuarioId);
        System.out.println(ok ? "Te uniste correctamente." : "No fue posible unirse al partido.");
    }

    private static void mostrarPartido(PartidoDTO p) {
        System.out.println("----------------------------");
        System.out.println("ID: " + p.getId());
        System.out.println("Deporte: " + p.getDeporte().getNombre());
        System.out.println("Duración: " + p.getDuracion() + " minutos");
        System.out.println("Ubicación: " + p.getUbicacion());
        System.out.println("Fecha y hora: " + p.getFechaHora());
        System.out.println("Estado: " + (p.getEstado() != null ? p.getEstado().getNombre() : "Sin estado"));
        System.out.println("----------------------------");
    }

    public static Date parsearAFecha(String fechaStr) {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            return formato.parse(fechaStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Fecha inválida. Usa el formato dd/MM/yyyy HH:mm", e);
        }
    }
}
