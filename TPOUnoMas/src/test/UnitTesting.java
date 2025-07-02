// Código de prueba completo con todos los patrones aplicados y al menos un jugador seleccionado

package test;

import controller.DeporteController;
import controller.PartidoController;
import controller.UsuarioController;
import enums.NivelJuego;
import enums.TipoEmparejamiento;
import model.dto.DeporteDTO;
import model.dto.DeporteUsuarioDTO;
import model.dto.PartidoDTO;
import model.dto.UsuarioDTO;
import model.entity.*;
import model.estadosDelPartido.*;
import services.notificacionService.EmailServiceAdapter;
import services.notificacionService.NotificadorEmail;
import services.notificacionService.NotificadorPush;
import services.notificacionService.interfaces.IStrategyNotificacion;
import strategy.*;

import java.util.*;

public class UnitTesting {

	public static void main(String[] args) throws InterruptedException {
		testPushStrategy();
		testEstadosPartido();
		testEmparejamientoStrategy();
		System.out.println("============================");
		System.out.println("TEST COMPLETO FINALIZADO");
	}

	private static Partido prepararPartidoYJugadores() {
		Deporte futbol = new Deporte(null, "Fútbol", 2);
		Partido partido = new Partido(futbol, 90, "Palermo", new Date(), "org-001", TipoEmparejamiento.NIVEL);

		Usuario u1 = new Usuario("Juan", "juan@mail.com", "123", "Palermo");
		Usuario u2 = new Usuario("Ana", "ana@mail.com", "456", "Belgrano");

		List<DeporteUsuario> preferencias1 = new ArrayList<>();
		preferencias1.add(new DeporteUsuario(futbol, NivelJuego.INTERMEDIO, true));
		u1.setDeportesUsuario(preferencias1);

		List<DeporteUsuario> preferencias2 = new ArrayList<>();
		preferencias2.add(new DeporteUsuario(futbol, NivelJuego.AVANZADO, false));
		u2.setDeportesUsuario(preferencias2);

		partido.agregarJugadorDirecto(u1);
		partido.agregarJugadorDirecto(u2);

		return partido;
	}

	public static void testEstadosPartido() throws InterruptedException {
		System.out.println("============================");
		System.out.println("TESTING CICLO COMPLETO DE ESTADOS (PATRÓN STATE)");

		Deporte futbol = new Deporte(null, "Fútbol", 2);
		Partido partido = new Partido(futbol, 1, "Palermo", new Date(System.currentTimeMillis() + 2000), "org-1", TipoEmparejamiento.NIVEL); // empieza en 2 seg
		PartidoContext contexto = new PartidoContext(partido);

		Usuario u1 = new Usuario("Juan", "juan@mail.com", "123", "Palermo");
		Usuario u2 = new Usuario("Ana", "ana@mail.com", "456", "Belgrano");

		System.out.println("➤ Estado inicial: " + partido.getEstado());
		contexto.agregarJugador(u1);
		contexto.agregarJugador(u2); // debería transicionar a PartidoArmado

		System.out.println("➤ Estado tras agregar jugadores: " + partido.getEstado());

		try {
			contexto.finalizar();
			System.out.println("➤ Estado tras confirmar por hora: " + partido.getEstado());

			contexto.finalizar();
			System.out.println("➤ Estado tras iniciar partido: " + partido.getEstado());

			contexto.finalizar(); // Finaliza
			System.out.println("➤ Estado tras finalizar: " + partido.getEstado());

			contexto.cancelar("Intento inválido");
		} catch (Exception e) {
			System.out.println("✔ No se puede cancelar un partido finalizado: " + e.getMessage());
		}
	}


	public static void testPushStrategy() {
		System.out.println("============================");
		System.out.println("TESTING PATRÓN STRATEGY (PUSH)");

		Partido partido = prepararPartidoYJugadores();
		IStrategyNotificacion pushNotificador = new NotificadorPush("APIKEY-123");

		pushNotificador.notificarNuevoPartido(partido, partido.getJugadoresInscritos());
		pushNotificador.notificarCambioEstado(partido, "En juego");
	}

	public static void testEmparejamientoStrategy() {
		System.out.println("============================");
		System.out.println("TESTING ESTRATEGIA DE EMPAREJAMIENTO (USANDO MÉTODOS DEL SERVICIO)");

		DeporteController deporteController = DeporteController.getInstance();
		UsuarioController usuarioController = UsuarioController.getInstance();
		PartidoController partidoController = PartidoController.getInstance();

		// Crear deporte
		DeporteDTO futbolDTO = new DeporteDTO(null, "Fútbol", 3);
		deporteController.crearDeporte(futbolDTO);
		DeporteDTO futbol = deporteController.buscarPorNombre("Fútbol");

		// Crear partido
		String organizador = "org@mail.com";
		UsuarioDTO organizadorDTO = new UsuarioDTO("Org", organizador, "123", "Palermo");
		try {
			usuarioController.registrar(organizadorDTO);
		} catch (Exception ignored) {}

		PartidoDTO partidoDTO = new PartidoDTO(futbol, 90, "Palermo", new Date(), organizador, TipoEmparejamiento.NIVEL);
		String partidoId = partidoController.crearPartido(partidoDTO);
		partidoController.agregarJugador(partidoId, organizador);

		// Crear candidatos
		String[][] datos = {
				{"Juan", "juan@mail.com", "Palermo", "INTERMEDIO"},
				{"Ana", "ana@mail.com", "Belgrano", "AVANZADO"},
				{"Luis", "luis@mail.com", "Avellaneda", "PRINCIPIANTE"},
				{"Mia", "mia@mail.com", "CABA", "INTERMEDIO"},
				{"Leo", "leo@mail.com", "Tigre", "AVANZADO"}
		};

		for (String[] d : datos) {
			UsuarioDTO u = new UsuarioDTO(d[0], d[1], "123", d[2]);
			try {
				usuarioController.registrar(u);
			} catch (Exception ignored) {}

			NivelJuego nivel = NivelJuego.valueOf(d[3]);
			usuarioController.actualizarPreferencias(d[1], List.of(new DeporteUsuarioDTO(futbol, nivel, false)));
		}

		// Estrategia por Nivel
		partidoController.cambiarTipoEmparejamiento(partidoId, TipoEmparejamiento.NIVEL);
		List<UsuarioDTO> nivelEmp = partidoController.emparejar(partidoId);
		System.out.println("➤ Emparejamiento por NIVEL:");
		nivelEmp.forEach(u -> System.out.println("- " + u.getEmail()));

		// Estrategia por Cercanía
		partidoController.cambiarTipoEmparejamiento(partidoId, TipoEmparejamiento.CERCANIA);
		List<UsuarioDTO> cercaniaEmp = partidoController.emparejar(partidoId);
		System.out.println("➤ Emparejamiento por CERCANÍA:");
		cercaniaEmp.forEach(u -> System.out.println("- " + u.getEmail()));

		// Estrategia por Historial
		partidoController.cambiarTipoEmparejamiento(partidoId, TipoEmparejamiento.HISTORIAL);
		List<UsuarioDTO> historialEmp = partidoController.emparejar(partidoId);
		System.out.println("➤ Emparejamiento por HISTORIAL:");
		historialEmp.forEach(u -> System.out.println("- " + u.getEmail()));
	}




}
