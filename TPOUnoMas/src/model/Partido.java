package model;

import state.EstadoPartido;
import state.NecesitamosJugadores;
import strategy.*;
import observer.*;
import adapter.*;
import service.*;
import repository.*;
import controller.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.HashMap;
import java.util.Map;

public class Partido {
    private String id;
    private Deporte deporte;
    private int duracion;
    private String ubicacion;
    private Date fechaHora;
    private List<Usuario> jugadoresInscritos;
    private EstadoPartido estado;
    private List<Notificacion> notificaciones;
    
    public Partido() {
        this.jugadoresInscritos = new ArrayList<>();
        this.notificaciones = new ArrayList<>();
        this.estado = new NecesitamosJugadores();
    }
    
    public Partido(Deporte deporte, int duracion, String ubicacion, Date fechaHora) {
        this();
        this.deporte = deporte;
        this.duracion = duracion;
        this.ubicacion = ubicacion;
        this.fechaHora = fechaHora != null ? new Date(fechaHora.getTime()) : null;
    }
    
    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public Deporte getDeporte() { return deporte; }
    public void setDeporte(Deporte deporte) { this.deporte = deporte; }
    
    public int getDuracion() { return duracion; }
    public void setDuracion(int duracion) { this.duracion = duracion; }
    
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    
    public Date getFechaHora() { return fechaHora; }
    public void setFechaHora(Date fechaHora) { this.fechaHora = fechaHora; }
    
    public List<Usuario> getJugadoresInscritos() { return new ArrayList<>(jugadoresInscritos); }
    public void setJugadoresInscritos(List<Usuario> jugadoresInscritos) {
        this.jugadoresInscritos = jugadoresInscritos != null ? new ArrayList<>(jugadoresInscritos) : new ArrayList<>();
    }
    
    public EstadoPartido getEstado() { return estado; }
    public void setEstado(EstadoPartido estado) { this.estado = estado; }
    
    public List<Notificacion> getNotificaciones() { return new ArrayList<>(notificaciones); }
    public void setNotificaciones(List<Notificacion> notificaciones) {
        this.notificaciones = notificaciones != null ? new ArrayList<>(notificaciones) : new ArrayList<>();
    }
    
    // Métodos de lógica de negocio
    public boolean agregarJugadorDirecto(Usuario jugador) {
        if (jugador == null || jugadoresInscritos.contains(jugador)) return false;
        return jugadoresInscritos.add(jugador);
    }
    
    public boolean removerJugador(Usuario jugador) {
        return jugador != null && jugadoresInscritos.remove(jugador);
    }
    
    public boolean estaCompleto() {
        if (deporte == null) return false;
        int jugadoresNecesarios = deporte.getCantidadJugadoresEstandar();
        return jugadoresInscritos.size() >= jugadoresNecesarios;
    }
    
    public boolean estaInscrito(Usuario usuario) {
        return usuario != null && jugadoresInscritos.contains(usuario);
    }
    
    public void agregarNotificacion(Notificacion notificacion) {
        if (notificacion != null) {
            if (notificacion.getIdPartido() == null && this.id != null) {
                notificacion.setIdPartido(this.id);
            }
            notificaciones.add(notificacion);
        }
    }
    
    public long getHorasHastaPartido() {
        if (fechaHora == null) return 0;
        long tiempoActual = System.currentTimeMillis();
        long tiempoPartido = fechaHora.getTime();
        long diferencia = tiempoPartido - tiempoActual;
        return diferencia / (60 * 60 * 1000); // Convertir a horas
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Partido partido = (Partido) obj;
        return Objects.equals(id, partido.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Partido{id='" + id + "', deporte=" + 
               (deporte != null ? deporte.getNombre() : "null") + 
               ", ubicacion='" + ubicacion + "', estado=" + 
               (estado != null ? estado.getNombre() : "null") + "}";
    }
    
    // ===== MÉTODO MAIN PARA DEMOSTRACIÓN DEL SISTEMA =====
    public static void main(String[] args) {
        System.out.println("=== SISTEMA UNO MAS - DEMOSTRACIÓN ===\n");
        
        try {
            // 1. Crear repositorios
            UsuarioRepository usuarioRepo = new UsuarioRepository();
            PartidoRepository partidoRepo = new PartidoRepository();
            
            // 2. Crear servicios
            NotificacionService notificacionService = new NotificacionService();
            UsuarioService usuarioService = new UsuarioService(usuarioRepo);
            PartidoService partidoService = new PartidoService(partidoRepo, notificacionService, usuarioRepo);
            EmparejamientoService emparejamientoService = new EmparejamientoService();
            
            // 3. Configurar notificadores (Observer pattern)
            EmailServiceAdapter emailAdapter = new EmailServiceAdapter();
            emailAdapter.autenticar("test@example.com", "password123");
            
            NotificadorEmail notificadorEmail = new NotificadorEmail(emailAdapter);
            NotificadorPush notificadorPush = new NotificadorPush("firebase-api-key-123");
            
            notificacionService.agregarObservador(notificadorEmail);
            notificacionService.agregarObservador(notificadorPush);
            
            // 4. Crear controladores
            UsuarioController usuarioController = new UsuarioController(usuarioService);
            PartidoController partidoController = new PartidoController(partidoService, emparejamientoService, usuarioService);
            
            // 5. Crear deportes
            System.out.println("1. Creando deportes...");
            Deporte futbol = new Deporte("FUTBOL", "Fútbol", 11);
            Deporte basquet = new Deporte("BASQUET", "Básquet", 5);
            Deporte tenis = new Deporte("TENIS", "Tenis", 1);
            
            // 6. Crear usuarios
            System.out.println("2. Registrando usuarios...");
            
            Usuario juan = new Usuario("juan@email.com", "Juan Pérez", "123456", "Palermo, CABA");
            juan.setId("USR_001");
            juan.agregarDeporteUsuario(new DeporteUsuario(futbol, NivelJuego.INTERMEDIO, true));
            juan.agregarDeporteUsuario(new DeporteUsuario(basquet, NivelJuego.PRINCIPIANTE, false));
            
            Usuario maria = new Usuario("maria@email.com", "María González", "123456", "Recoleta, CABA");
            maria.setId("USR_002");
            maria.agregarDeporteUsuario(new DeporteUsuario(futbol, NivelJuego.AVANZADO, true));
            maria.agregarDeporteUsuario(new DeporteUsuario(tenis, NivelJuego.INTERMEDIO, false));
            
            Usuario carlos = new Usuario("carlos@email.com", "Carlos López", "123456", "Belgrano, CABA");
            carlos.setId("USR_003");
            carlos.agregarDeporteUsuario(new DeporteUsuario(basquet, NivelJuego.AVANZADO, true));
            carlos.agregarDeporteUsuario(new DeporteUsuario(futbol, NivelJuego.INTERMEDIO, false));
            
            Usuario ana = new Usuario("ana@email.com", "Ana Martínez", "123456", "Villa Crespo, CABA");
            ana.setId("USR_004");
            ana.agregarDeporteUsuario(new DeporteUsuario(tenis, NivelJuego.PRINCIPIANTE, true));
            ana.agregarDeporteUsuario(new DeporteUsuario(futbol, NivelJuego.PRINCIPIANTE, false));
            
            // Registrar usuarios en el sistema
            usuarioController.registrarUsuario(juan);
            usuarioController.registrarUsuario(maria);
            usuarioController.registrarUsuario(carlos);
            usuarioController.registrarUsuario(ana);
            
            System.out.println("✓ Usuarios registrados: " + usuarioRepo.contarUsuarios());
            
            // 7. Demostrar patrón Strategy - Diferentes estrategias de emparejamiento
            System.out.println("\n3. Configurando estrategias de emparejamiento (Strategy Pattern)...");
            
            // Estrategia por nivel
            EstrategiaEmparejamiento estrategiaNivel = new EmparejamientoPorNivel(NivelJuego.PRINCIPIANTE, NivelJuego.AVANZADO);
            
            // Estrategia por cercanía
            EstrategiaEmparejamiento estrategiaCercania = new EmparejamientoPorCercania(15); // 15 km de radio
            
            // Estrategia por historial
            EstrategiaEmparejamiento estrategiaHistorial = new EmparejamientoPorHistorial(0); // Acepta jugadores nuevos
            
            // 8. Crear partidos demostrando patrón State
            System.out.println("\n4. Creando partidos (State Pattern)...");
            
            // Partido de fútbol
            Date fechaPartido1 = new Date(System.currentTimeMillis() + 3600000); // En 1 hora
            Partido partidoFutbol = new Partido(futbol, 90, "Cancha Municipal Palermo", fechaPartido1);
            partidoFutbol.setId("PAR_001");
            
            System.out.println("Estado inicial: " + partidoFutbol.getEstado().getNombre());
            
            // Crear partido usando el controlador con estrategia por nivel
            partidoController.setEstrategiaEmparejamiento(estrategiaNivel);
            Partido partidoCreado = partidoController.crearPartido(partidoFutbol, estrategiaNivel);
            
            System.out.println("✓ Partido creado: " + partidoCreado.toString());
            
            // 9. Demostrar transiciones de estado agregando jugadores
            System.out.println("\n5. Demostrando transiciones de estado...");
            
            // Agregar jugadores uno por uno para ver cambios de estado
            System.out.println("Agregando jugadores al partido...");
            
            boolean resultado1 = partidoController.unirseAPartido(partidoCreado.getId(), juan.getId());
            System.out.println("Juan se unió: " + resultado1 + " - Estado: " + partidoCreado.getEstado().getNombre());
            
            boolean resultado2 = partidoController.unirseAPartido(partidoCreado.getId(), maria.getId());
            System.out.println("María se unió: " + resultado2 + " - Estado: " + partidoCreado.getEstado().getNombre());
            
            // Agregar más jugadores para completar el equipo (simulación)
            for (int i = 3; i <= futbol.getCantidadJugadoresEstandar(); i++) {
                Usuario jugadorTemp = new Usuario("jugador" + i + "@email.com", "Jugador " + i, "123456", "CABA");
                jugadorTemp.setId("USR_" + String.format("%03d", i + 1));
                jugadorTemp.agregarDeporteUsuario(new DeporteUsuario(futbol, NivelJuego.INTERMEDIO, false));
                usuarioController.registrarUsuario(jugadorTemp);
                
                partidoController.unirseAPartido(partidoCreado.getId(), jugadorTemp.getId());
                System.out.println("Jugador " + i + " se unió - Estado: " + partidoCreado.getEstado().getNombre());
                
                if (partidoCreado.estaCompleto()) {
                    System.out.println("¡Partido completo! Transicionando a estado: " + partidoCreado.getEstado().getNombre());
                    break;
                }
            }
            
            // 10. Demostrar diferentes estrategias
            System.out.println("\n6. Demostrando diferentes estrategias de emparejamiento...");
            
            // Crear candidatos para emparejamiento
            List<Usuario> candidatos = new ArrayList<>();
            candidatos.add(carlos);
            candidatos.add(ana);
            
            // Probar estrategia por cercanía
            partidoController.setEstrategiaEmparejamiento(estrategiaCercania);
            List<Usuario> seleccionadosCercania = emparejamientoService.emparejarJugadores(partidoCreado, candidatos);
            System.out.println("Estrategia por cercanía seleccionó: " + seleccionadosCercania.size() + " jugadores");
            
            // Probar estrategia por historial
            partidoController.setEstrategiaEmparejamiento(estrategiaHistorial);
            List<Usuario> seleccionadosHistorial = emparejamientoService.emparejarJugadores(partidoCreado, candidatos);
            System.out.println("Estrategia por historial seleccionó: " + seleccionadosHistorial.size() + " jugadores");
            
            // 11. Demostrar búsquedas con filtros
            System.out.println("\n7. Demostrando búsquedas con filtros...");
            
            Map<String, Object> filtros = new HashMap<>();
            filtros.put("deporte", futbol);
            filtros.put("ubicacion", "Palermo");
            
            List<Partido> partidosEncontrados = partidoController.buscarPartidos(filtros);
            System.out.println("Partidos encontrados con filtros: " + partidosEncontrados.size());
            
            // 12. Mostrar notificaciones (Observer Pattern en acción)
            System.out.println("\n8. Demostrando sistema de notificaciones (Observer Pattern)...");
            System.out.println("Notificaciones en el partido: " + partidoCreado.getNotificaciones().size());
            
            for (Notificacion notif : partidoCreado.getNotificaciones()) {
                System.out.println("- " + notif.getMensaje());
            }
            
            // 13. Demostrar cancelación de partido
            System.out.println("\n9. Demostrando cancelación de partido...");
            System.out.println("Estado antes de cancelar: " + partidoCreado.getEstado().getNombre());
            
            boolean cancelado = partidoController.cancelarPartido(partidoCreado.getId());
            System.out.println("Partido cancelado: " + cancelado);
            System.out.println("Estado después de cancelar: " + partidoCreado.getEstado().getNombre());
            
            // 14. Mostrar estadísticas finales
            System.out.println("\n10. Estadísticas finales del sistema:");
            System.out.println("- Usuarios registrados: " + usuarioRepo.contarUsuarios());
            System.out.println("- Partidos creados: " + partidoRepo.contarPartidos());
            System.out.println("- Observadores de notificaciones: " + notificacionService.getCantidadObservadores());
            System.out.println("- Notificaciones generadas: " + partidoCreado.getNotificaciones().size());
            
            // 15. Demostrar Adapter Pattern
            System.out.println("\n11. Demostrando Adapter Pattern con diferentes servicios de email...");
            
            // Cambiar a otro tipo de servicio de email
            EmailServiceAdapter outLookAdapter = new EmailServiceAdapter(EmailServiceAdapter.TipoServicio.OUTLOOK);
            outLookAdapter.autenticar("admin@sistema.com", "admin123");
            
            NotificadorEmail notificadorOutlook = new NotificadorEmail(outLookAdapter);
            
            // Enviar notificación de prueba
            List<Usuario> usuariosPrueba = new ArrayList<>();
            usuariosPrueba.add(juan);
            notificadorOutlook.notificarNuevoPartido(partidoCreado, usuariosPrueba);
            
            System.out.println("\n=== DEMOSTRACIÓN COMPLETADA EXITOSAMENTE ===");
            System.out.println("\nPatrones implementados:");
            System.out.println("✓ MVC - Separación en capas (Model, Controller, Service, Repository)");
            System.out.println("✓ State - Estados del partido (Necesitamos jugadores → Partido armado → Confirmado → En juego → Finalizado/Cancelado)");
            System.out.println("✓ Strategy - Diferentes algoritmos de emparejamiento (por nivel, cercanía, historial)");
            System.out.println("✓ Observer - Sistema de notificaciones (email y push)");
            System.out.println("✓ Adapter - Adaptación de diferentes servicios de email");
            
        } catch (Exception e) {
            System.err.println("Error durante la demostración: " + e.getMessage());
            e.printStackTrace();
        }
    }
}